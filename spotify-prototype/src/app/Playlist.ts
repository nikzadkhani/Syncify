import {Song} from './Song'

export default class Playlist {
  name: String;
  ownerId: ObjectId;
  created: Date;
  updated: Date;
  shareWithIds: [ObjectId];
  songs: [Song];
}
