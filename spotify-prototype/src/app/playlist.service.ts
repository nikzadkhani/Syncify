import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {

  uri = 'http://localhost:4000/playlist';

  constructor(private http: HttpClient) { }

  addPlaylist(playlist_name) {
    const obj = {
      name: playlist_name,
    };
    console.log(obj);
    this.http.post(`${this.uri}/add`, obj)
        .subscribe(res => console.log('Done'));
  }

  getPlaylists() {
      return this
             .http
             .get(`${this.uri}`);
  }

  editPlaylists(id) {
      return this
              .http
              .get(`${this.uri}/edit/${id}`);
  }

  updatePlaylist(id, playlist_name) {

      const obj = {
          name: playlist_name
      };
      this
        .http
        .post(`${this.uri}/update/${id}`, obj)
        .subscribe(res => console.log('Done'));
  }

  deletePlaylist(id) {
      return this
                .http
                .get(`${this.uri}/delete/${id}`);
  }

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

  removeSongFromPlaylist(id, song_id) {
    this
      .http
      .get(`${this.uri}/removesong/${id}/${song_id}`);
  }

}

