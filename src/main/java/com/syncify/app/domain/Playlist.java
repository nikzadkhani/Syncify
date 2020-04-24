package com.syncify.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Playlist.
 */
@Entity
@Table(name = "playlist")
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "syncify_id")
    private UUID syncifyId;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @ManyToOne
    @JsonIgnoreProperties("playlistIds")
    private Song songId;

    @ManyToMany(mappedBy = "playlistIds")
    @JsonIgnore
    private Set<UserDetails> userIds = new HashSet<>();

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

    public Playlist syncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
        return this;
    }

    public void setSyncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
    }

    public String getName() {
        return name;
    }

    public Playlist name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public Playlist author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Song getSongId() {
        return songId;
    }

    public Playlist songId(Song song) {
        this.songId = song;
        return this;
    }

    public void setSongId(Song song) {
        this.songId = song;
    }

    public Set<UserDetails> getUserIds() {
        return userIds;
    }

    public Playlist userIds(Set<UserDetails> userDetails) {
        this.userIds = userDetails;
        return this;
    }

    public Playlist addUserId(UserDetails userDetails) {
        this.userIds.add(userDetails);
        userDetails.getPlaylistIds().add(this);
        return this;
    }

    public Playlist removeUserId(UserDetails userDetails) {
        this.userIds.remove(userDetails);
        userDetails.getPlaylistIds().remove(this);
        return this;
    }

    public void setUserIds(Set<UserDetails> userDetails) {
        this.userIds = userDetails;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Playlist)) {
            return false;
        }
        return id != null && id.equals(((Playlist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Playlist{" +
            "id=" + getId() +
            ", syncifyId='" + getSyncifyId() + "'" +
            ", name='" + getName() + "'" +
            ", author='" + getAuthor() + "'" +
            "}";
    }
}
