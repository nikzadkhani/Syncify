package com.syncify.app.service;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AppleQueryUtils {


    private final Logger log = LoggerFactory.getLogger(AppleQueryUtils.class);

    private int responseCode;
    private String responseTrimmed;
    private JsonObject jsonObject;
    private String keyId;
    private String teamId;
    private String privateKey;

    public AppleQueryUtils(String keyId, String teamId, String privateKey) {
        log.info("creating apple query utils with teamId {} and keyId {}", teamId, keyId );
        this.keyId = keyId;
        this.teamId = teamId;
        this.privateKey = privateKey;
        this.responseCode = 0;
        this.responseTrimmed = "";
        this.jsonObject = null;
    }

    private PrivateKey getPrivateKey() {

        final PEMParser pemParser = new PEMParser(new StringReader(privateKey));
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        final PrivateKeyInfo object;
        try {
            object = (PrivateKeyInfo) pemParser.readObject();
            return converter.getPrivateKey(object);
        } catch (Exception e) {
            log.error("cannot create key object for {}", privateKey);
        }
        return null;
    }

    private String generateJWT(PrivateKey pKey) {

        return Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, keyId)
            .setIssuer(teamId)
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(pKey, SignatureAlgorithm.ES256)
            .compact();
    }


    public JsonObject getJsonResponseForLink(String link) {

        String response = "";
        BufferedReader in = null;

        String authString = null;
        try {
            authString = createAuthString();
        } catch (Exception e) {
            log.error("Could not create Auth String reason: {}",e.getMessage());
        }

        try {
            URL url = new URL(link);

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();


            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Authorization",  authString);
            this.responseCode = httpCon.getResponseCode();

            log.debug("Response Code : " + responseCode);
            if (this.responseCode != 200) {
                return null;
            }

            in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }

        } catch (MalformedURLException ex) {
            log.error("MalformedURLException!!");
        } catch (ProtocolException ex) {
            log.error("ProtocolException!!");
        } catch (IOException ex) {
            log.error("IOException!!");
        } catch (Exception e) {
            log.error(e.getMessage());
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

    private String createAuthString() throws Exception {
        return "Bearer " + generateJWT(getPrivateKey());
    }
}
