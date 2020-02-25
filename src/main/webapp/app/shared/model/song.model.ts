import { IPlaylist } from 'app/shared/model/playlist.model';

export interface ISong {
  id?: number;
  artist?: string;
  name?: string;
  spotifyId?: string;
  appleId?: string;
  playlists?: IPlaylist[];
}

export class Song implements ISong {
  constructor(
    public id?: number,
    public artist?: string,
    public name?: string,
    public spotifyId?: string,
    public appleId?: string,
    public playlists?: IPlaylist[]
  ) {}
}
