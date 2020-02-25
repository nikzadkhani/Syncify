import { IPlaylist } from 'app/shared/model/playlist.model';

export interface IUserDetails {
  id?: number;
  username?: string;
  password?: string;
  platformUserName?: string;
  playlists?: IPlaylist[];
}

export class UserDetails implements IUserDetails {
  constructor(
    public id?: number,
    public username?: string,
    public password?: string,
    public platformUserName?: string,
    public playlists?: IPlaylist[]
  ) {}
}
