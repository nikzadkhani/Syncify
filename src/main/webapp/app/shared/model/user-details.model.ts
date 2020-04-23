import { IPlaylist } from 'app/shared/model/playlist.model';

export interface IUserDetails {
  id?: number;
  syncifyId?: string;
  platformUserName?: string;
  playlistIds?: IPlaylist[];
}

export class UserDetails implements IUserDetails {
  constructor(public id?: number, public syncifyId?: string, public platformUserName?: string, public playlistIds?: IPlaylist[]) {}
}
