package com.syncify.app.service;

import com.syncify.app.SyncifyApp;
import com.syncify.app.domain.Song;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SyncifyApp.class)
@ActiveProfiles(profiles = "test-secrets")
@Transactional
class SpotifyQueryUtilsTest {

    @Value("${secret.client-id}")
    private String clientId;

    @Value("${secret.client-secret}")
    private String clientSecret;

    private final Logger log = LoggerFactory.getLogger(SpotifyQueryUtilsTest.class);


    @Test
    public void getFirstSongForQuery() {
        SpotifyQueryUtils spotifyQueryUtils = new SpotifyQueryUtils(clientId, clientSecret);
        Song firstSongResults = spotifyQueryUtils.getSongFromSearchTerm("despacito");
        assertThat(firstSongResults.getName().contains("despacito"));
        assertThat(firstSongResults.getIsrc().contains("USUM71607007"));
        assertThat(firstSongResults.getArtist().contains("Luis Fonsi"));
        assertThat(firstSongResults.getSpotifyURL().contains("https://api.spotify.com/v1/tracks/6habFhsOp2NvshLv26DqMb"));
    }

}
