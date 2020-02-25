import { ISong } from 'app/shared/model/song.model';
import { IUserDetails } from 'app/shared/model/user-details.model';

export interface IPlaylist {
  id?: number;
  name?: string;
  author?: string;
  song?: ISong;
  users?: IUserDetails[];
}

export class Playlist implements IPlaylist {
  constructor(public id?: number, public name?: string, public author?: string, public song?: ISong, public users?: IUserDetails[]) {}
}
