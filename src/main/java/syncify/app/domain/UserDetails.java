package syncify.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserDetails.
 */
@Entity
@Table(name = "user_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "platform_user_name")
    private String platformUserName;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_details_playlist",
               joinColumns = @JoinColumn(name = "user_details_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "id"))
    private Set<Playlist> playlists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public UserDetails playlists(Set<Playlist> playlists) {
        this.playlists = playlists;
        return this;
    }

    public UserDetails addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
        playlist.getUsers().add(this);
        return this;
    }

    public UserDetails removePlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
        playlist.getUsers().remove(this);
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
            ", platformUserName='" + getPlatformUserName() + "'" +
            "}";
    }
}
