package com.syncify.app.service;

import com.syncify.app.SyncifyApp;
import com.syncify.app.config.TestSecurityConfiguration;
import com.syncify.app.domain.Song;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SyncifyApp.class, TestSecurityConfiguration.class})
@Transactional
public class AppleMusicServiceIT {
    private final Logger log = LoggerFactory.getLogger(AppleMusicServiceIT.class);

    @Autowired
    private AppleMusic appleMusic;

    @Test
    public void returnsSongOfFirstResult(){
        Song firstSongResults = appleMusic.getSongFromSearchTerm("despacito");
        assertThat(firstSongResults.getName().contains("despacito"));
        assertThat(firstSongResults.getIsrc().contains("USUM71607007"));
        assertThat(firstSongResults.getArtist().contains("Luis Fonsi & Daddy Yankee"));
        assertThat(firstSongResults.getAppleURL().contains("https://music.apple.com/us/album/despacito/1447401519?i=1447401620"));
    }

    @Test
    public void addAppleURLToExistingSong(){
        Song spotifySong = new Song();
        spotifySong.setName("despacito");
        spotifySong.setIsrc("USUM71607007");
        spotifySong.setArtist("Luis Fonsi & Daddy Yankee");
        appleMusic.updateSongWithAppleURL(spotifySong);
        assertThat(spotifySong.getAppleURL().contains("https://music.apple.com/us/album/despacito/1447401519?i=1447401620"));
    }
}
