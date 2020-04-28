package com.syncify.app.service;

import com.syncify.app.SyncifyApp;
import com.syncify.app.config.TestSecurityConfiguration;
import com.syncify.app.domain.Song;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SyncifyApp.class, TestSecurityConfiguration.class})
@Transactional
class SpotifyMusicTest {

    @Autowired
    SpotifyMusic spotifyMusic;

    private final Logger log = LoggerFactory.getLogger(SpotifyMusicTest.class);

    @Test
    public void getFirstSongForQuery() {
        Song firstSongResults = spotifyMusic.getSongFromSearchTerm("despacito");
        assertThat(firstSongResults.getName().contains("despacito"));
        assertThat(firstSongResults.getIsrc().contains("USUM71607007"));
        assertThat(firstSongResults.getArtist().contains("Luis Fonsi"));
        assertThat(firstSongResults.getSpotifyURL().contains("https://api.spotify.com/v1/tracks/6habFhsOp2NvshLv26DqMb"));
    }

    @Test
    public void getSpotifyURLToExistingSong(){
        Song appleSong = new Song();
        appleSong.setName("despacito");
        appleSong.setIsrc("USUM71607007");
        appleSong.setArtist("Luis Fonsi & Daddy Yankee");
        spotifyMusic.updateSongWithSpotifyURL(appleSong);
        assertThat(appleSong.getSpotifyURL().contains("https://api.spotify.com/v1/tracks/6habFhsOp2NvshLv26DqMb"));
    }
}
