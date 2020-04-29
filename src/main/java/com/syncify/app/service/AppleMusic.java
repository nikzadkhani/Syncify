package com.syncify.app.service;

import com.syncify.app.domain.Song;

public interface AppleMusic {
    Song getSongFromSearchTerm(String searchTerm);

    void updateSongWithAppleURL(Song spotifySong);
}
