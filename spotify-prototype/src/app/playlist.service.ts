import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// This is the playlist service, call this for any backend needs.
// See Playlist.ts and Song.ts for data definitions
@Injectable({
  providedIn: 'root'
})
export class PlaylistService {

  uri = 'http://localhost:4000/playlist';

  constructor(private http: HttpClient) { }

  // This creates a new playlist with the given name
  addPlaylist(playlist_name) {
    const obj = {
      name: playlist_name,
    };
    console.log(obj);
    this.http.post(`${this.uri}/add`, obj)
        .subscribe(res => console.log('Done'));
  }

  // This returns a list of all playlists
  getPlaylists() {
      return this
             .http
             .get(`${this.uri}`);
  }

  // This returns the playlist object for the given playlist ID
  editPlaylists(id) {
      return this
              .http
              .get(`${this.uri}/edit/${id}`);
  }

  // This updates the playlist for the given playlist ID
  updatePlaylist(id, playlist_name) {

      const obj = {
          name: playlist_name
      };
      this
        .http
        .post(`${this.uri}/update/${id}`, obj)
        .subscribe(res => console.log('Done'));
  }

  // Deletes the playlist of the given playlist ID
  deletePlaylist(id) {
      return this
                .http
                .get(`${this.uri}/delete/${id}`);
  }

  // This adds a song to the playlist of 'id', must provide song name, artist, and album
  addSongToPlaylist(id, song_name, song_artist, song_album) {
    const obj = {
      song_name: song_name,
      song_artist: song_artist,
      song_album: song_album
    };
    this
      .http
      .post(`${this.uri}/addsong/${id}`, obj)
      .subscribe(res => console.log('Done'));
  }

  // This removes a song from the playlist 'id' and song id
  removeSongFromPlaylist(id, song_id) {
    this
      .http
      .get(`${this.uri}/removesong/${id}/${song_id}`);
  }

}

