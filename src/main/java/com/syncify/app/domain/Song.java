package com.syncify.app.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Song.
 */
@Entity
@Table(name = "song")
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "syncify_id")
    private UUID syncifyId;

    @Column(name = "isrc")
    private String isrc;

    @Column(name = "name")
    private String name;

    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;

    @Column(name = "spotify_url")
    private String spotifyURL;

    @Column(name = "apple_url")
    private String appleURL;

    @OneToMany(mappedBy = "songId")
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

    public Song syncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
        return this;
    }

    public void setSyncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
    }

    public String getIsrc() {
        return isrc;
    }

    public Song isrc(String isrc) {
        this.isrc = isrc;
        return this;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public String getName() {
        return name;
    }

    public Song name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public Song artist(String artist) {
        this.artist = artist;
        return this;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public Song album(String album) {
        this.album = album;
        return this;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSpotifyURL() {
        return spotifyURL;
    }

    public Song spotifyURL(String spotifyURL) {
        this.spotifyURL = spotifyURL;
        return this;
    }

    public void setSpotifyURL(String spotifyURL) {
        this.spotifyURL = spotifyURL;
    }

    public String getAppleURL() {
        return appleURL;
    }

    public Song appleURL(String appleURL) {
        this.appleURL = appleURL;
        return this;
    }

    public void setAppleURL(String appleURL) {
        this.appleURL = appleURL;
    }

    public Set<Playlist> getPlaylistIds() {
        return playlistIds;
    }

    public Song playlistIds(Set<Playlist> playlists) {
        this.playlistIds = playlists;
        return this;
    }

    public Song addPlaylistId(Playlist playlist) {
        this.playlistIds.add(playlist);
        playlist.setSongId(this);
        return this;
    }

    public Song removePlaylistId(Playlist playlist) {
        this.playlistIds.remove(playlist);
        playlist.setSongId(null);
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
        if (!(o instanceof Song)) {
            return false;
        }
        return id != null && id.equals(((Song) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Song{" +
            "id=" + getId() +
            ", syncifyId='" + getSyncifyId() + "'" +
            ", isrc='" + getIsrc() + "'" +
            ", name='" + getName() + "'" +
            ", artist='" + getArtist() + "'" +
            ", album='" + getAlbum() + "'" +
            ", spotifyURL='" + getSpotifyURL() + "'" +
            ", appleURL='" + getAppleURL() + "'" +
            "}";
    }
}
