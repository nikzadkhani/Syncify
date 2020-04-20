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
public class Spotify {

    private final Logger log = LoggerFactory.getLogger(Spotify.class);

    @Value("${secret.key-id}")
    private String keyId;

    @Value("${secret.team-id}")
    private String teamId;

    @Value("${secret.private-key}")
    private String privateKey;

    private String spotifyApiUrl = "https://api.spotify.com/v1/search"; // From https://developer.spotify.com/documentation/web-api/reference/search/search/

    public JsonObject getJsonFromUrl(String link){
        SpotifyQueryUtils spotifyQueryUtils  = new SpotifyQueryUtils(teamId,keyId, privateKey);
        try {
            return spotifyQueryUtils.getJson(link);
        }catch(Exception e){
            throw new RuntimeException("could not get security key or connect to spotify: "+e);
        }
    }

    public Song getSongFromSearchTerm(String searchTerm) {
        SpotifyQueryUtils spotifyQueryUtils = new SpotifyQueryUtils(teamId,keyId, privateKey);
        JsonObject responseJson = spotifyQueryUtils.getJson(spotifyApiUrl+searchTerm);
        return makeFirstSongFromJsonObject(responseJson);
    }

    private Song makeFirstSongFromJsonObject(JsonObject responseJson) {
        System.out.println(responseJson.toString());
        JsonObject songJson = responseJson.getAsJsonObject("results")
            .getAsJsonObject("songs")
            .getAsJsonArray("data")
            .get(0)
            .getAsJsonObject()
            .getAsJsonObject("attributes");

        Song song = new Song();
        song.setName(songJson.get("name").getAsString());
        song.setArtist(songJson.get("artistName").getAsString());
        song.setAlbum(songJson.get("label").getAsString());
        song.setIsrc(songJson.get("isrc").getAsString());
        song.setSpotifyURL(songJson.get("url").getAsString());
        return song;
    }
}
