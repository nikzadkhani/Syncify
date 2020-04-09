package com.syncify.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.syncify.app.SyncifyApp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SyncifyApp.class)
@ActiveProfiles(profiles = "test-secrets")
@Transactional
public class AppleMusicServiceIT {


    @Autowired
    private AppleMusic appleMusicService;



    @Test
    public void verifyWeCanConnectToAppleMusicApi(){
        appleMusicService.connectToAppleMusicApli();
    }

}
