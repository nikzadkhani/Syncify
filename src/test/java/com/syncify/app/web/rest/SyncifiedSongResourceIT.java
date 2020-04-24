package com.syncify.app.web.rest;

import com.syncify.app.SyncifyApp;
import com.syncify.app.domain.Song;
import com.syncify.app.domain.SongRequest;
import com.syncify.app.repository.SongRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SongResource} REST controller.
 */
@SpringBootTest(classes = SyncifyApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SyncifiedSongResourceIT {

    private static final UUID DEFAULT_SYNCIFY_ID = UUID.randomUUID();
    private static final String DEFAULT_ISRC = "USUM71607007";
    private static final String DEFAULT_NAME = "Despacito";
    private static final String DEFAULT_ARTIST = "Luis Fonsi & Daddy Yankee";
    private static final String DEFAULT_ALBUM = "VIDA";
    private static final String DEFAULT_SPOTIFY_URL = "https://api.spotify.com/v1/tracks/6habFhsOp2NvshLv26DqMb";
    private static final String DEFAULT_APPLE_URL = "https://music.apple.com/us/album/despacito/1447401519?i=1447401620";
    private static SongRequest SONG_REQUEST = new SongRequest(DEFAULT_SYNCIFY_ID,DEFAULT_NAME);

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MockMvc restSongMockMvc;


    @Test
    @Transactional
    public void createSong() throws Exception {
        int databaseSizeBeforeCreate = songRepository.findAll().size();

        // Create the Song
        restSongMockMvc.perform(post("/api/syncifiedSongs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(SONG_REQUEST)))
            .andExpect(status().isCreated());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeCreate + 1);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getSyncifyId()).isNotNull();
        assertThat(testSong.getIsrc()).isEqualTo(DEFAULT_ISRC);
        assertThat(testSong.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSong.getArtist()).isEqualTo(DEFAULT_ARTIST);
        assertThat(testSong.getAlbum()).isEqualTo(DEFAULT_ALBUM);
        assertThat(testSong.getSpotifyURL()).isEqualTo(DEFAULT_SPOTIFY_URL);
        assertThat(testSong.getAppleURL()).isEqualTo(DEFAULT_APPLE_URL);
    }
}

