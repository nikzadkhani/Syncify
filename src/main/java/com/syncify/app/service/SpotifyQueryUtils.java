package com.syncify.app.service;


import com.syncify.app.domain.Song;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
public class SpotifyQueryUtils {


    private final SpotifyApi spotifyApi;
    private final ClientCredentialsRequest clientCredentialsRequest;
    public static final String TYPE = ModelObjectType.TRACK.getType();

    public SpotifyQueryUtils( @Value("${secret.client-id}") String clientId, @Value("${secret.client-secret}") String clientSecret){
        spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
        clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();
    }

    public Song getSongFromSearchTerm(String songName) {
        updateClientCredentials();

        Track firstTrack = getTrackForSongName(songName);

        return mapTrackToSong(firstTrack);
    }

    private Song mapTrackToSong(Track firstTrack) {
        Song firstSong = new Song();
        firstSong.setSpotifyURL(firstTrack.getHref());
        firstSong.setName(firstTrack.getName());
        firstSong.setIsrc(firstTrack.getExternalIds().getExternalIds().get("isrc"));
        firstSong.setArtist(firstTrack.getArtists()[0].getName());
        firstSong.setAlbum(firstTrack.getAlbum().getName());
        return firstSong;
    }

    private Track getTrackForSongName(String songName) {
        SearchItemRequest searchItemRequest = spotifyApi.searchItem(songName, TYPE)
            .build();

        Track firstTrack = null;
        try {
            final SearchResult searchResult = searchItemRequest.execute();
            firstTrack = searchResult.getTracks().getItems()[0];

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return firstTrack;
    }

    private void updateClientCredentials() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}