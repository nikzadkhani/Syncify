package com.syncify.app.repository;

import com.syncify.app.domain.Song;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Song entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findSongByName(String name);
}
