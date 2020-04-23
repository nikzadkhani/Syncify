package com.syncify.app.web.rest;

import com.syncify.app.SyncifyApp;
import com.syncify.app.domain.Playlist;
import com.syncify.app.repository.PlaylistRepository;

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
 * Integration tests for the {@link PlaylistResource} REST controller.
 */
@SpringBootTest(classes = SyncifyApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PlaylistResourceIT {

    private static final UUID DEFAULT_SYNCIFY_ID = UUID.randomUUID();
    private static final UUID UPDATED_SYNCIFY_ID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaylistMockMvc;

    private Playlist playlist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Playlist createEntity(EntityManager em) {
        Playlist playlist = new Playlist()
            .syncifyId(DEFAULT_SYNCIFY_ID)
            .name(DEFAULT_NAME)
            .author(DEFAULT_AUTHOR);
        return playlist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Playlist createUpdatedEntity(EntityManager em) {
        Playlist playlist = new Playlist()
            .syncifyId(UPDATED_SYNCIFY_ID)
            .name(UPDATED_NAME)
            .author(UPDATED_AUTHOR);
        return playlist;
    }

    @BeforeEach
    public void initTest() {
        playlist = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlaylist() throws Exception {
        int databaseSizeBeforeCreate = playlistRepository.findAll().size();

        // Create the Playlist
        restPlaylistMockMvc.perform(post("/api/playlists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isCreated());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeCreate + 1);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getSyncifyId()).isEqualTo(DEFAULT_SYNCIFY_ID);
        assertThat(testPlaylist.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlaylist.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    public void createPlaylistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playlistRepository.findAll().size();

        // Create the Playlist with an existing ID
        playlist.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaylistMockMvc.perform(post("/api/playlists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlaylists() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList
        restPlaylistMockMvc.perform(get("/api/playlists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].syncifyId").value(hasItem(DEFAULT_SYNCIFY_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)));
    }
    
    @Test
    @Transactional
    public void getPlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get the playlist
        restPlaylistMockMvc.perform(get("/api/playlists/{id}", playlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playlist.getId().intValue()))
            .andExpect(jsonPath("$.syncifyId").value(DEFAULT_SYNCIFY_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR));
    }

    @Test
    @Transactional
    public void getNonExistingPlaylist() throws Exception {
        // Get the playlist
        restPlaylistMockMvc.perform(get("/api/playlists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Update the playlist
        Playlist updatedPlaylist = playlistRepository.findById(playlist.getId()).get();
        // Disconnect from session so that the updates on updatedPlaylist are not directly saved in db
        em.detach(updatedPlaylist);
        updatedPlaylist
            .syncifyId(UPDATED_SYNCIFY_ID)
            .name(UPDATED_NAME)
            .author(UPDATED_AUTHOR);

        restPlaylistMockMvc.perform(put("/api/playlists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlaylist)))
            .andExpect(status().isOk());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getSyncifyId()).isEqualTo(UPDATED_SYNCIFY_ID);
        assertThat(testPlaylist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlaylist.getAuthor()).isEqualTo(UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void updateNonExistingPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Create the Playlist

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaylistMockMvc.perform(put("/api/playlists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeDelete = playlistRepository.findAll().size();

        // Delete the playlist
        restPlaylistMockMvc.perform(delete("/api/playlists/{id}", playlist.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
