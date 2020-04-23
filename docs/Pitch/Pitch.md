# Assignment 1 - Pitches

Group Members: Nikzad Khani, Mark White, Parker Stone and Raymond Chu

## Primary Idea: Cross-Platform Shared Playlist Web App

API's that will be used:
  1. Spotify API
  2. iTunes API
  3. Spotify OAuth
  4. Apple Music/iTunes OAuth
  5. Database to store preferences/playlist info
  6. Google OAuth
  7. Gmail API

Our primary idea is to create a website, that will allow you
and a group of friends to keep a shared playlist with each other.
The way it will work is that a user will create an account
with the app on our website. They will link their own streaming
service iTunes or Spotify. Then the user can invite other users
on the website to their playlist. Invites will be sent via email.
Once the invite is accepted the shared playlist will share
the playlist with live updates on all the invitees respective
platforms. This will all be done by spamming api calls on the
backend. In the event that the song doesn't exist on both
platforms, the song will not show up on the playlist that doesn't
have the song. Furthermore, users can log in to the website and
see which songs are not transferred.

## Secondary Ideas: Crime Prevention App

API's that will be used:
  1. Twilio api
  2. Boston Crime API - has a csv of all crimes from now since 2015
  3. Database to store preferences/playlist info
  4. Google OAuth

 Our secondary idea is an app that will let users know when they
 are entering an unsafe area in Boston at a time where there is
 a high probability of crime. Essentially we will look at the
 crime csv and figure out which areas have higher crime rates than
 other areas at specific times. Another feature we thought about
 is using Twilio to text users when they are entering a crime area
 or to tell users to leave an area that has a high probability of
 crime. If we don't end up using location data, we can have
 users enter their interested areas on the website, and we can
 alert them when there is a higher crime rate than usual in the
 areas they have enable to get info from on the website.
