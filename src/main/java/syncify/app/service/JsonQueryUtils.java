package com.syncify.app.service;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * This class is intended to make the API call to retrieve the JWT
 */
public class JsonQueryUtils {

    private int responseCode;
    private String responseTrimmed;
    private JsonObject jsonObject;
    //a Preferences object to store the token, avoiding repetitive calls
    private Preferences preferences = Preferences.userNodeForPackage(JsonQueryUtils.class);
    private String keyId = "R7SQHMWJKU";
    private String teamId = "A76HA22K75";
    private String secretPath = "/Users/matt/mark/syncify-test/src/main/resources/secret.p8";
    private String privateKey;

    public JsonQueryUtils(String keyId, String teamId, String privateKey) {
        this.keyId=keyId;
        this.teamId=teamId;
        this.privateKey=privateKey;
        this.responseCode = 0;
        this.responseTrimmed = "";
        this.jsonObject = null;
    }

    private PrivateKey getPrivateKey() throws Exception {
//read your key
        //String path = new PathResource(secretPath).getFile().getAbsolutePath();

        final PEMParser pemParser = new PEMParser(new StringReader(privateKey));
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

        return converter.getPrivateKey(object);
    }

    private String generateJWT(PrivateKey pKey) throws Exception {
        if (pKey == null) {
            pKey = getPrivateKey();
        }

        String token = Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, keyId)
            .setIssuer(teamId)
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(pKey, SignatureAlgorithm.ES256)
            .compact();

        return token;
    }


    public JsonObject getJson(String link) throws NoSuchAlgorithmException {

        String response = "";
        BufferedReader in = null;

        try {
            URL url = new URL(link);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            String basicAuth = "";

            long prefExpiry = preferences.getLong("expiry",0);

            System.out.println("prefExpiry: " + prefExpiry);
            System.out.println("System.currentTimeMillis(): " + System.currentTimeMillis());

            String prefBuilder = preferences.get("builderString","");

            System.out.println("preferences.get(builderString,0): " + preferences.get("builderString",""));

            if(preferences.getLong("expiry",0) < System.currentTimeMillis() || preferences.get("builderString","") == null) {
                basicAuth = "Bearer " + generateJWT(getPrivateKey());
                System.out.println("###getToken()###");
            } else {
                basicAuth = "Bearer " + preferences.get("builderString","");
                System.out.println("LINE 128 basicAuth: " + basicAuth);
                System.out.println("Token retrieved from preferences");
            }

            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Authorization", basicAuth);
            this.responseCode = httpCon.getResponseCode();

            System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            Map<String, List<String>> headers = httpCon.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                if (key != null) {
                    if (key.equals("Retry-After")) {
                        List<String> value = entry.getValue();
                        //this.retryAfterSeconds = value.get(0);

                        //System.out.println("RETRY AFTER: " + this.retryAfterSeconds + " seconds!");
//                        throw new CustomException(seconds);
                    } else {
                        //System.out.println("NO RETRY AFTER!");
                    }
                }
                System.out.println("Header Name: " + key);
                List<String> value = entry.getValue();
                System.out.println("Header Value: " + value.get(0));

            }

            if (this.responseCode != 200) {
                return null;

            }

            in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String inputLine;

            while((inputLine = in.readLine()) != null) {
                response += inputLine;
            }
            //in.close();

        } catch (MalformedURLException ex) {
            System.out.println("MalformedURLException!!");
        } catch (ProtocolException ex) {
            System.out.println("ProtocolException!!");
        } catch (IOException ex) {
            System.out.println("IOException!!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.responseTrimmed = response.trim();
        this.jsonObject = new JsonParser().parse(responseTrimmed).getAsJsonObject();

        System.out.println("jsonObject from JsonQueryUtils: " + this.jsonObject.toString());

        return this.jsonObject;
    }
}
