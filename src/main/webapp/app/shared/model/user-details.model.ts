import { IPlaylist } from 'app/shared/model/playlist.model';

export interface IUserDetails {
  id?: number;
  platformUserName?: string;
  playlists?: IPlaylist[];
}

export class UserDetails implements IUserDetails {
  constructor(public id?: number, public platformUserName?: string, public playlists?: IPlaylist[]) {}
}
