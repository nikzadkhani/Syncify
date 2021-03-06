package com.syncify.app.web.rest;

import com.syncify.app.domain.Song;
import com.syncify.app.domain.SongRequest;
import com.syncify.app.repository.SongRepository;
import com.syncify.app.service.AppleMusicWeb;
import com.syncify.app.service.CachedAppleMusic;
import com.syncify.app.service.SpotifyMusic;

import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for managing {@link com.syncify.app.domain.Song}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SyncifiedSongResource {

    private final Logger log = LoggerFactory.getLogger(SyncifiedSongResource.class);

    private static final String ENTITY_NAME = "song";


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SongRepository songRepository;

    private AppleMusicWeb appleMusic;

    private SpotifyMusic spotifyMusic;

    private CachedAppleMusic cachedAppleMusic;

    public SyncifiedSongResource(SongRepository songRepository, AppleMusicWeb appleMusic, SpotifyMusic spotifyMusic) {
        this.songRepository = songRepository;
        this.appleMusic = appleMusic;
        this.spotifyMusic = spotifyMusic;
        this.cachedAppleMusic = new CachedAppleMusic(appleMusic, songRepository);
    }

    /**
     * {@code POST  /syncifiedSongs} : Create a new song.
     *
     * @param songRequest the song to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new song, or with status {@code 400 (Bad Request)} if the song has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/syncifiedSongs")
    public ResponseEntity<Song> createSong(@RequestBody SongRequest songRequest) throws URISyntaxException {
        log.debug("REST request to save Song : {}", songRequest);
        Song song = cachedAppleMusic.getSongFromSearchTerm(songRequest.getName());
        spotifyMusic.updateSongWithSpotifyURL(song);
        song.setSyncifyId(songRequest.getSyncifyId());

        Song result = songRepository.save(song);
        return ResponseEntity.created(new URI("/api/songs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
