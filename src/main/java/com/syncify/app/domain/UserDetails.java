package com.syncify.app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A UserDetails.
 */
@Entity
@Table(name = "user_details")
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "syncify_id")
    private UUID syncifyId;

    @Column(name = "platform_user_name")
    private String platformUserName;

    @ManyToMany
    @JoinTable(name = "user_details_playlist_id",
               joinColumns = @JoinColumn(name = "user_details_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "playlist_id_id", referencedColumnName = "id"))
    private Set<Playlist> playlistIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getSyncifyId() {
        return syncifyId;
    }

    public UserDetails syncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
        return this;
    }

    public void setSyncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
    }

    public String getPlatformUserName() {
        return platformUserName;
    }

    public UserDetails platformUserName(String platformUserName) {
        this.platformUserName = platformUserName;
        return this;
    }

    public void setPlatformUserName(String platformUserName) {
        this.platformUserName = platformUserName;
    }

    public Set<Playlist> getPlaylistIds() {
        return playlistIds;
    }

    public UserDetails playlistIds(Set<Playlist> playlists) {
        this.playlistIds = playlists;
        return this;
    }

    public UserDetails addPlaylistId(Playlist playlist) {
        this.playlistIds.add(playlist);
        playlist.getUserIds().add(this);
        return this;
    }

    public UserDetails removePlaylistId(Playlist playlist) {
        this.playlistIds.remove(playlist);
        playlist.getUserIds().remove(this);
        return this;
    }

    public void setPlaylistIds(Set<Playlist> playlists) {
        this.playlistIds = playlists;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetails)) {
            return false;
        }
        return id != null && id.equals(((UserDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
            "id=" + getId() +
            ", syncifyId='" + getSyncifyId() + "'" +
            ", platformUserName='" + getPlatformUserName() + "'" +
            "}";
    }
}
