package com.syncify.app.repository;

import com.syncify.app.domain.Playlist;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the Playlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> getPlaylistBySyncifyId(UUID syncifyId);

}
