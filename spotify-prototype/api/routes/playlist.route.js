const express = require('express');
const app = express();
const playlistRoutes = express.Router();

// Require Playlist model in our routes module
let Playlist = require('../models/Playlist');
let Song = require('../models/Playlist')

// Defined store route
playlistRoutes.route('/add').post(function (req, res) {
  let playlist = new Playlist(req.body);
  playlist.save()
    .then(playlist => {
      res.status(200).json({'playlist': 'playlist added successfully'});
    })
    .catch(err => {
      res.status(400).send("unable to save to database");
    });
});

// Defined get data(index or listing) route
playlistRoutes.route('/').get(function (req, res) {
    Playlist.find(function (err, playlists){
    if(err){
      console.log(err);
    }
    else {
      res.json(playlists);
    }
  });
});

// Defined edit route
// TODO: just get a specific playlist, rename edit to get
playlistRoutes.route('/edit/:id').get(function (req, res) {
  let id = req.params.id;
  Playlist.findById(id, function (err, playlist){
      res.json(playlist);
  });
});

//  Defined update route
playlistRoutes.route('/update/:id').post(function (req, res, next) {
    Playlist.findById(req.params.id, function(err, playlist) {
    if (!playlist)
      return next(new Error('Could not load Playlist'));
    else {
        playlist.name = req.body.name;
        playlist.updated = Date.now;

        playlist.save().then(playlist => {
          res.json('Update complete');
      })
      .catch(err => {
            res.status(400).send("unable to update the database");
      });
    }
  });
});

// Defined delete | remove | destroy route
playlistRoutes.route('/delete/:id').get(function (req, res) {
    Playlist.findByIdAndRemove({_id: req.params.id}, function(err, playlist){
        if(err) res.json(err);
        else res.json('Playlist successfully removed');
    });
});

// Define remove a song
playlistRoutes.route('/removesong/:id/:songId').get(function (req, res) {
    Playlist.findById({_id: req.params.id}, function(err, playlist){
        if(err) res.json(err);
        else {
          let songId = req.params.songId;
          if (songId) {
            playlist.songs = playlist.songs.filter(song => {song._id !== songId});
            playlist.save().then(playlist => {
              res.json('song removed');
            })
          }
          else {
            res.status(400).send("Song ID not specified");
          }

        }
    });
});

// Define add a song
playlistRoutes.route('/addsong/:id').post(function (req, res, next) {
    Playlist.findById({_id: req.params.id}, function(err, playlist){
      if (!playlist) {
          console.log('could not load playlist')
          return next(new Error('Could not load Playlist'));
      } else {
        if (err) {
          res.json(err);
        } else {
        // TODO: prevent duplicate song name + artist
          let song = new Song(req.body);
          playlist.songs.push(song);
          playlist.save().then(playlist => {
            console.log('song added')
            res.json('song added');
          })
        }
      }
    });
});

// TODO: figure out user info
// TODO: add a route for sharing playlist and removing people from playlist

module.exports = playlistRoutes;
