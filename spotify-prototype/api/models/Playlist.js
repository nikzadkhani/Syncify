const mongoose = require('mongoose');
const Schema = mongoose.Schema;

let Person = new Schema({
  name: String
})

let Song = new Schema({
  addedByUserId: {
    type: 'ObjectId',
    ref: 'Person'
  },
  dateAdded: {
    type: Date,
    default: Date.now
  },
  song_name: String,
  song_artist: String,
  song_album: String
})

// Define collection and schema for Playlist
let Playlist = new Schema({
  name: {
    type: String
  },
  ownerId: {
    type: 'ObjectId',
    ref: 'Person'
  },
  created: {
    type: Date,
    default: Date.now
  },
  updated: {
    type: Date,
    default: Date.now
  },
  shareWithIds: [{
    type: 'ObjectId',
    ref: 'Person'
  }],
  songs: [ Song ]

},{
    collection: 'playlist'
});

module.exports = mongoose.model('Playlist', Playlist);
