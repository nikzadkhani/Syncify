package com.syncify.app.service;

import com.google.gson.JsonObject;
import com.syncify.app.domain.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator {@code AuditEventRepository}.
 */
@Service
@Transactional
public class AppleMusicWeb implements AppleMusic {

    private final Logger log = LoggerFactory.getLogger(AppleMusicWeb.class);

    private String keyId;
    private String teamId;
    private String privateKey;

    public AppleMusicWeb(@Value("${secret.key-id}") String keyId, @Value("${secret.team-id}") String teamId, @Value("${secret.private-key}") String privateKey) {
        this.keyId = keyId;
        this.teamId = teamId;
        this.privateKey = privateKey;
    }


    private String appleApiUrl = "https://api.music.apple.com/v1/catalog/us/search?term=";

    public JsonObject getJsonFromUrl(String link){
        AppleQueryUtils appleQueryUtils = new AppleQueryUtils(teamId,keyId, privateKey);
        try {
            return appleQueryUtils.getJsonResponseForLink(link);
        }catch(Exception e){
            throw new RuntimeException("could not get security key or connect to apple: "+e);
        }
    }

    @Override
    public Song getSongFromSearchTerm(String searchTerm) {
        AppleQueryUtils appleQueryUtils = new AppleQueryUtils(teamId,keyId, privateKey);
        JsonObject responseJson = appleQueryUtils.getJsonResponseForLink(appleApiUrl+searchTerm);
        return makeFirstSongFromJsonObject(responseJson);
    }

    private Song makeFirstSongFromJsonObject(JsonObject responseJson) {
        JsonObject songJson = responseJson.getAsJsonObject("results")
            .getAsJsonObject("songs")
            .getAsJsonArray("data")
            .get(0)
            .getAsJsonObject()
            .getAsJsonObject("attributes");

        Song song = new Song();
        song.setName(songJson.get("name").getAsString());
        song.setArtist(songJson.get("artistName").getAsString());
        song.setAlbum(songJson.get("albumName").getAsString());
        song.setIsrc(songJson.get("isrc").getAsString());
        song.setAppleURL(songJson.get("url").getAsString());
        return song;
    }

    @Override
    public void updateSongWithAppleURL(Song spotifySong) {
        Song tempSong = getSongFromSearchTerm(spotifySong.getName());
        spotifySong.setAppleURL(tempSong.getAppleURL());

    }
}
