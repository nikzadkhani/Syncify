package com.syncify.app.web.rest;

import com.syncify.app.SyncifyApp;
import com.syncify.app.domain.Song;
import com.syncify.app.repository.SongRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
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
    private static final UUID UPDATED_SYNCIFY_ID = UUID.randomUUID();

    private static final String DEFAULT_ISRC = "AAAAAAAAAA";
    private static final String UPDATED_ISRC = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM = "BBBBBBBBBB";

    private static final String DEFAULT_SPOTIFY_URL = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_URL = "BBBBBBBBBB";

    private static final String DEFAULT_APPLE_URL = "AAAAAAAAAA";
    private static final String UPDATED_APPLE_URL = "BBBBBBBBBB";

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSongMockMvc;

    private Song song;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Song createEntity(EntityManager em) {
        Song song = new Song()
            .syncifyId(DEFAULT_SYNCIFY_ID)
            .isrc(DEFAULT_ISRC)
            .name(DEFAULT_NAME)
            .artist(DEFAULT_ARTIST)
            .album(DEFAULT_ALBUM)
            .spotifyURL(DEFAULT_SPOTIFY_URL)
            .appleURL(DEFAULT_APPLE_URL);
        return song;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Song createUpdatedEntity(EntityManager em) {
        Song song = new Song()
            .syncifyId(UPDATED_SYNCIFY_ID)
            .isrc(UPDATED_ISRC)
            .name(UPDATED_NAME)
            .artist(UPDATED_ARTIST)
            .album(UPDATED_ALBUM)
            .spotifyURL(UPDATED_SPOTIFY_URL)
            .appleURL(UPDATED_APPLE_URL);
        return song;
    }

    @BeforeEach
    public void initTest() {
        song = createEntity(em);
    }

    @Test
    @Transactional
    public void createSong() throws Exception {
        int databaseSizeBeforeCreate = songRepository.findAll().size();

        // Create the Song
        restSongMockMvc.perform(post("/api/syncifiedsongs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isCreated());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeCreate + 1);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getSyncifyId()).isEqualTo(DEFAULT_SYNCIFY_ID);
        assertThat(testSong.getIsrc()).isEqualTo(DEFAULT_ISRC);
        assertThat(testSong.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSong.getArtist()).isEqualTo(DEFAULT_ARTIST);
        assertThat(testSong.getAlbum()).isEqualTo(DEFAULT_ALBUM);
        assertThat(testSong.getSpotifyURL()).isEqualTo(DEFAULT_SPOTIFY_URL);
        assertThat(testSong.getAppleURL()).isEqualTo(DEFAULT_APPLE_URL);
    }

    @Test
    @Transactional
    public void createSongWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = songRepository.findAll().size();

        // Create the Song with an existing ID
        song.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSongMockMvc.perform(post("/api/songs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSongs() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get all the songList
        restSongMockMvc.perform(get("/api/songs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(song.getId().intValue())))
            .andExpect(jsonPath("$.[*].syncifyId").value(hasItem(DEFAULT_SYNCIFY_ID.toString())))
            .andExpect(jsonPath("$.[*].isrc").value(hasItem(DEFAULT_ISRC)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].artist").value(hasItem(DEFAULT_ARTIST)))
            .andExpect(jsonPath("$.[*].album").value(hasItem(DEFAULT_ALBUM)))
            .andExpect(jsonPath("$.[*].spotifyURL").value(hasItem(DEFAULT_SPOTIFY_URL)))
            .andExpect(jsonPath("$.[*].appleURL").value(hasItem(DEFAULT_APPLE_URL)));
    }

    /*
    @Test
    @Transactional
    public void getSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get the song
        restSongMockMvc.perform(get("/api/songs/{id}", song.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(song.getId().intValue()))
            .andExpect(jsonPath("$.syncifyId").value(DEFAULT_SYNCIFY_ID.toString()))
            .andExpect(jsonPath("$.isrc").value(DEFAULT_ISRC))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.artist").value(DEFAULT_ARTIST))
            .andExpect(jsonPath("$.album").value(DEFAULT_ALBUM))
            .andExpect(jsonPath("$.spotifyURL").value(DEFAULT_SPOTIFY_URL))
            .andExpect(jsonPath("$.appleURL").value(DEFAULT_APPLE_URL));
    }

    @Test
    @Transactional
    public void getNonExistingSong() throws Exception {
        // Get the song
        restSongMockMvc.perform(get("/api/songs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song
        Song updatedSong = songRepository.findById(song.getId()).get();
        // Disconnect from session so that the updates on updatedSong are not directly saved in db
        em.detach(updatedSong);
        updatedSong
            .syncifyId(UPDATED_SYNCIFY_ID)
            .isrc(UPDATED_ISRC)
            .name(UPDATED_NAME)
            .artist(UPDATED_ARTIST)
            .album(UPDATED_ALBUM)
            .spotifyURL(UPDATED_SPOTIFY_URL)
            .appleURL(UPDATED_APPLE_URL);

        restSongMockMvc.perform(put("/api/songs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSong)))
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getSyncifyId()).isEqualTo(UPDATED_SYNCIFY_ID);
        assertThat(testSong.getIsrc()).isEqualTo(UPDATED_ISRC);
        assertThat(testSong.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSong.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testSong.getAlbum()).isEqualTo(UPDATED_ALBUM);
        assertThat(testSong.getSpotifyURL()).isEqualTo(UPDATED_SPOTIFY_URL);
        assertThat(testSong.getAppleURL()).isEqualTo(UPDATED_APPLE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Create the Song

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongMockMvc.perform(put("/api/songs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeDelete = songRepository.findAll().size();

        // Delete the song
        restSongMockMvc.perform(delete("/api/songs/{id}", song.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeDelete - 1);
    }
     */
}
