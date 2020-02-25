package com.syncify.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Song.
 */
@Entity
@Table(name = "song")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "artist")
    private String artist;

    @Column(name = "name")
    private String name;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "apple_id")
    private String appleId;

    @OneToMany(mappedBy = "song")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Playlist> playlists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSpotifyId() {
        return spotifyId;
    }

    public Song spotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
        return this;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getAppleId() {
        return appleId;
    }

    public Song appleId(String appleId) {
        this.appleId = appleId;
        return this;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public Song playlists(Set<Playlist> playlists) {
        this.playlists = playlists;
        return this;
    }

    public Song addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
        playlist.setSong(this);
        return this;
    }

    public Song removePlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
        playlist.setSong(null);
        return this;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
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
            ", artist='" + getArtist() + "'" +
            ", name='" + getName() + "'" +
            ", spotifyId='" + getSpotifyId() + "'" +
            ", appleId='" + getAppleId() + "'" +
            "}";
    }
}
