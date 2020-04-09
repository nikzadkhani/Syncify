package com.syncify.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator {@code AuditEventRepository}.
 */
@Service
@Transactional
public class AppleMusic {

    private final Logger log = LoggerFactory.getLogger(AppleMusic.class);

    @Value("${secret.key-id}")
    private String keyId;

    @Value("${secret.team-id}")
    private String teamId;

    @Value("${secret.private-key}")
    private String privateKey;


    public void connectToAppleMusicApli(){
        JsonQueryUtils appleQueryUtils = new JsonQueryUtils(teamId,keyId, privateKey);
        try {
            appleQueryUtils.getJson("https://api.music.apple.com/v1/catalog/us/playlists/pl.14362d3dfe4b41f7878939782647e0ba");
        }catch(Exception e){
            throw new RuntimeException("could not get security key or connect to apple: "+e);
        }
    }

}
