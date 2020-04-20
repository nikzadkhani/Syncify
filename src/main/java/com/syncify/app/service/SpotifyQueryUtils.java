package com.syncify.app.service;

//TODO
//update URIs
// Update JSON Struct
//Update Security Keys
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.PrivateKey;
import java.util.Date;

/**
 * This class is intended to make the API call to retrieve the JWT
 */
public class SpotifyQueryUtils {

    private int responseCode;
    private String responseTrimmed;
    private JsonObject jsonObject;
    private String keyId;
    private String teamId;
    private String privateKey;

    public SpotifyQueryUtils(String keyId, String teamId, String privateKey) {
        this.keyId = keyId;
        this.teamId = teamId;
        this.privateKey = privateKey;
        this.responseCode = 0;
        this.responseTrimmed = "";
        this.jsonObject = null;
    }

    private PrivateKey getPrivateKey() throws Exception {

        final PEMParser pemParser = new PEMParser(new StringReader(privateKey));
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

        return converter.getPrivateKey(object);
    }

    private String generateJWT(PrivateKey pKey) {

        String token = Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, keyId)
            .setIssuer(teamId)
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(pKey, SignatureAlgorithm.ES256)
            .compact();

        return token;
    }


    public JsonObject getJson(String link) {

        String response = "";
        BufferedReader in = null;

        try {
            URL url = new URL(link);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            String basicAuth = "";

            basicAuth = "Bearer " + generateJWT(getPrivateKey());

            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Authorization", basicAuth);
            this.responseCode = httpCon.getResponseCode();

            System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            if (this.responseCode != 200) {
                return null;
            }

            in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }

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
        return new JsonParser().parse(responseTrimmed).getAsJsonObject();
    }
}
