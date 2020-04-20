package com.syncify.app.service;

import com.syncify.app.SyncifyApp;
import com.syncify.app.domain.Song;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SyncifyApp.class)
@ActiveProfiles(profiles = "test-secrets")
@Transactional
public class SpotifyServiceIT {
    private final Logger log = LoggerFactory.getLogger(SpotifyServiceIT.class);

    @Autowired
    private Spotify spotifyService;


    @Test
    public void returnsSongOfFirstResult(){
        Song firstSongResults = spotifyService.getSongFromSearchTerm("despacito");
        assertThat(firstSongResults.getName().contains("despacito"));
        assertThat(firstSongResults.getIsrc().contains("USUM71607007"));
        assertThat(firstSongResults.getArtist().contains("Luis Fonsi & Daddy Yankee"));
        assertThat(firstSongResults.getAppleURL().contains("https://api.spotify.com/v1/search?q=despacito"));
    }

}
