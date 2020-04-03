package com.syncify.app.service;

import io.github.jhipster.config.JHipsterProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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

    //private final JHipsterProperties jHipsterProperties;

    public void connectToAppleMusicApli(){
        JsonQueryUtils jsonQueryUtils = new JsonQueryUtils();
        try {
            jsonQueryUtils.getJson("https://api.music.apple.com/v1/catalog/us/playlists/pl.14362d3dfe4b41f7878939782647e0ba");
        }catch(Exception e){
            throw new RuntimeException("could not get security key or connect to apple");
        }
    }







}
