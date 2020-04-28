package com.syncify.app.config;

import com.syncify.app.service.AppleMusic;
import com.syncify.app.service.SpotifyMusic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"dev", "test-secrets"})
@Configuration
public class MusicConfiguration {

    @Value("${secret.team-id}")
    private String teamId;

    @Value("${secret.private-key}")
    private String privateKey;

    @Value("${secret.key-id}")
    private String keyId;

    @Value("${secret.client-id}")
    private String clientId;

    @Value("${secret.client-secret}")
    private String clientSecret;

    @Bean
    AppleMusic appleMusic(){
        return new AppleMusic(keyId, teamId, privateKey) ;
    }

    @Bean
    SpotifyMusic spotifyMusic(){
        return new SpotifyMusic(clientId,clientSecret);
    }
}
