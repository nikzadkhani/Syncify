import { ISong } from 'app/shared/model/song.model';
import { IUserDetails } from 'app/shared/model/user-details.model';

export interface IPlaylist {
  id?: number;
  syncifyId?: string;
  name?: string;
  author?: string;
  songId?: ISong;
  userIds?: IUserDetails[];
}

export class Playlist implements IPlaylist {
  constructor(
    public id?: number,
    public syncifyId?: string,
    public name?: string,
    public author?: string,
    public songId?: ISong,
    public userIds?: IUserDetails[]
  ) {}
}
