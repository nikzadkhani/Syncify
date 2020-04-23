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
class SpotifyMusicTest {

    @Value("${secret.client-id}")
    private String clientId;

    @Value("${secret.client-secret}")
    private String clientSecret;

    private final Logger log = LoggerFactory.getLogger(SpotifyMusicTest.class);


    @Test
    public void getFirstSongForQuery() {
        SpotifyMusic spotifyMusic = new SpotifyMusic(clientId, clientSecret);
        Song firstSongResults = spotifyMusic.getSongFromSearchTerm("despacito");
        assertThat(firstSongResults.getName().contains("despacito"));
        assertThat(firstSongResults.getIsrc().contains("USUM71607007"));
        assertThat(firstSongResults.getArtist().contains("Luis Fonsi"));
        assertThat(firstSongResults.getSpotifyURL().contains("https://api.spotify.com/v1/tracks/6habFhsOp2NvshLv26DqMb"));
    }

    @Test
    public void getSpotifyURLToExistingSong(){
        SpotifyMusic spotifyMusic = new SpotifyMusic(clientId, clientSecret);
        Song appleSong = new Song();
        appleSong.setName("despacito");
        appleSong.setIsrc("USUM71607007");
        appleSong.setArtist("Luis Fonsi & Daddy Yankee");
        spotifyMusic.updateSongWithSpotifyURL(appleSong);
        assertThat(appleSong.getSpotifyURL().contains("https://api.spotify.com/v1/tracks/6habFhsOp2NvshLv26DqMb"));
    }
}