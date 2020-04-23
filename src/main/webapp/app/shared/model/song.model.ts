import { IPlaylist } from 'app/shared/model/playlist.model';

export interface ISong {
  id?: number;
  syncifyId?: string;
  isrc?: string;
  name?: string;
  artist?: string;
  album?: string;
  spotifyURL?: string;
  appleURL?: string;
  playlistIds?: IPlaylist[];
}

export class Song implements ISong {
  constructor(
    public id?: number,
    public syncifyId?: string,
    public isrc?: string,
    public name?: string,
    public artist?: string,
    public album?: string,
    public spotifyURL?: string,
    public appleURL?: string,
    public playlistIds?: IPlaylist[]
  ) {}
}
