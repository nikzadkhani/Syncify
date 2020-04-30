package com.syncify.app.service;

import com.syncify.app.domain.Song;
import com.syncify.app.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CachedAppleMusic implements AppleMusic {

    private final AppleMusicWeb appleMusicWeb;
    private final SongRepository songRepository;

    public CachedAppleMusic(AppleMusicWeb appleMusicWeb, SongRepository songRepository){

        this.appleMusicWeb = appleMusicWeb;
        this.songRepository = songRepository;
    }

    @Override
    public Song getSongFromSearchTerm(String searchTerm) {
        List<Song> foundSongs = songRepository.findSongByName(searchTerm);
        if(foundSongs.isEmpty()){
            return appleMusicWeb.getSongFromSearchTerm(searchTerm);
        }
        return foundSongs.get(0);

    }

    @Override
    public void updateSongWithAppleURL(Song spotifySong) {
        appleMusicWeb.updateSongWithAppleURL(spotifySong);
    }
}
