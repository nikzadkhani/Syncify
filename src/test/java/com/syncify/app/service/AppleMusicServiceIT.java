package com.syncify.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.syncify.app.SyncifyApp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SyncifyApp.class)
@Transactional
public class AppleMusicServiceIT {


    @Autowired
    private AppleMusic appleMusicService;


    @Test
    public void verifyWeCanRunTests(){
        assertThat(true).isTrue();
    }

    @Test
    public void verifyWeCanConnectToAppleMusicApi(){
        appleMusicService.connectToAppleMusicApli();

    }

}
