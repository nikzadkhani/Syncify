const mongoose = require('mongoose');

const dbHandler = require('./db-handler');
const routes = require('../routes/playlist.route');
const request = require('supertest');
const express = require('express');
const playlistModel = require('../models/Playlist');

const app = express();
var timeout = require('connect-timeout');
app.use(express.urlencoded({extended: false}));
app.use('/', routes);
app.use(timeout('30s'));


/**
 * Connect to a new in-memory database before running any tests.
 */
beforeAll(async () => await dbHandler.connect());

/**
 * Clear all test data after every test.
 */
afterEach(async () => await dbHandler.clearDatabase());

/**
 * Remove and close the db and server.
 */
afterAll(async () => await dbHandler.closeDatabase());

// test /
test('index route works', done => {
  request(app)
      .get("/")
      .expect("Content-Type", /json/)
      .expect([])
      .expect(200, done);
});


// test /add
test('add route works', done => {
  request(app)
      .post('/add')
      .type('form')
      .send({
        name: 'test playlist'
      })
      .then(() => {
        request(app)
          .get('/')
          .expect("Content-Type", /json/)
          .expect(200, done);
      })
});

// test /delete
test('delete route works', done => {
  // add a playlist first before deleting it
  request(app)
      .post('/add')
      .type('form')
      .send({
        name: 'test playlist'
      })
      .then(() => {
        // get the list of all playlists, should be 1
        request(app)
          .get('/')
          .expect("Content-Type", /json/)
          .expect(200)
          .expect(response => {
            var playlist = response.body[0]
            var playlistId = playlist._id
            // delete the playlist
            request(app)
              .get('/delete/' + playlistId)
              .expect(200)
              .expect(response => {
                expect(response.body).toEqual('Playlist successfully removed')
                // get the list of all playlists, should be none
                request(app)
                  .get('/')
                  .expect([])
                  .expect(200,done)
              })
          })
          .end(done)
      })
});


// test /update
test('update route works', done => {
  // add a playlist first before updating it
    request(app)
        .post('/add')
        .type('form')
        .send({
          name: 'test playlist'
        })
        .then(() => {
          // get the list of all playlists, should be 1
          request(app)
            .get('/')
            .expect("Content-Type", /json/)
            .expect(200)
            .expect(response => {
              var playlist = response.body[0]
              var playlistId = playlist._id
              // update the playlist name
              request(app)
                .post('/update/' + playlistId)
                .type('form')
                // TODO: test no fields get changed other than name
                .send({
                  name: 'new name'
                })
                .then(() => {
                  // read the playlist back and validate
                  request(app)
                    .get('/edit/' + playlistId)
                    .expect("Content-Type", /json/)
                    .expect(200)
                    .expect(repsonse => {
                      expect(repsonse.name).toEqual('new name')
                      expect(response.updated).toBeGreaterThan(response.created)
                    })
                })
            })
            .end(done)
        })
  });

// test /removesong
test('removesong route works', done => {
  request(app)
        .post('/add')
        .type('form')
        .send({
          name: 'test playlist'
        })
        .then(() => {
          // get the list of all playlists, should be 1
          request(app)
            .get('/')
            .expect("Content-Type", /json/)
            .expect(200)
            .expect(response => {
              var playlist = response.body[0]
              var playlistId = playlist._id
              // add song to playlist
              request(app)
                .post('/addsong/' + playlistId)
                .type('form')
                .send({
                  song_name: 'park',
                  song_artist: 'np'
                })
                .then(() => {
                  request(app)
                    .get('/edit/' + playlistId)
                    .expect("Content-Type", /json/)
                    .expect(200)
                    .expect(repsonse => {
                      expect(response.songs).toHaveLength(1)
                      const song = response.songs[0]
                      const songId = song._id
                      request(app)
                        .get('/removesong/' + playlistId + '/' + songId)
                        .expect('Content-Type', /json/)
                        .expect(200)
                        .expect('song removed')
                        .expect(response => {
                          request(app)
                            .get('/edit/' + playlistId)
                            .expect("Content-Type", /json/)
                            .expect(200)
                            .expect(repsonse => {
                              expect(response.songs).toHaveLength(0)
                            })
                        })
                    })
                })

            })
            .end(done)
        })
});


// test /addsong
test('addsong route works', done => {
  // add a playlist first before updating it
  request(app)
      .post('/add')
      .type('form')
      .send({
        name: 'test playlist'
      })
      .then(() => {
        // get the list of all playlists, should be 1
        request(app)
          .get('/')
          .expect("Content-Type", /json/)
          .expect(200)
          .expect(response => {
            var playlist = response.body[0]
            var playlistId = playlist._id
            // add song to playlist
            request(app)
              .post('/addsong/' + playlistId)
              .type('form')
              .send({
                song_name: 'park',
                song_artist: 'np'
              })
              .then(() => {
                request(app)
                  .get('/edit/' + playlistId)
                  .expect("Content-Type", /json/)
                  .expect(200)
                  .expect(repsonse => {
                    expect(response.songs).toHaveLength(1)
                    const song = response.songs[0]
                    expect(song.song_name).toEqual('park')
                    expect(song.song_artist).toEqual('np')
                    expect(song.dateAdded).toBeLessThan(new Date())
                  })
              })

          })
          .end(done)
      })
});


// test /edit
test('edit route works', done => {
  // add a playlist first before updating it
  request(app)
      .post('/add')
      .type('form')
      .send({
        name: 'test playlist'
      })
      .then(() => {
        // get the list of all playlists, should be 1
        request(app)
          .get('/')
          .expect("Content-Type", /json/)
          .expect(200)
          .expect(response => {
            var playlist = response.body[0]
            var playlistId = playlist._id
            // edit the playlist
            request(app)
              .get('/edit/' + playlistId)
              .expect("Content-Type", /json/)
              .expect(200)
              .expect(repsonse => {
                expect(repsonse.name).toEqual('test playlist')
                expect(response.updated).toBeLessThan(new Date())
                expect(response.created).toBeLessThan(new Date())
                expect(response.updated).toEqual(response.updated)
              })
          })
          .end(done)
      })
});

