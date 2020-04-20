package com.syncify.app.config;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Caffeine caffeine = jHipsterProperties.getCache().getCaffeine();

        CaffeineConfiguration caffeineConfiguration = new CaffeineConfiguration();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(caffeine.getMaxEntries()));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(TimeUnit.SECONDS.toNanos(caffeine.getTimeToLiveSeconds())));
        caffeineConfiguration.setStatisticsEnabled(true);
        jcacheConfiguration = caffeineConfiguration;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.syncify.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.syncify.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.syncify.app.domain.User.class.getName());
            createCache(cm, com.syncify.app.domain.Authority.class.getName());
            createCache(cm, com.syncify.app.domain.User.class.getName() + ".authorities");
            createCache(cm, com.syncify.app.domain.Song.class.getName());
            createCache(cm, com.syncify.app.domain.Song.class.getName() + ".playlists");
            createCache(cm, com.syncify.app.domain.Playlist.class.getName());
            createCache(cm, com.syncify.app.domain.Playlist.class.getName() + ".users");
            createCache(cm, com.syncify.app.domain.UserDetails.class.getName());
            createCache(cm, com.syncify.app.domain.UserDetails.class.getName() + ".playlists");
            createCache(cm, com.syncify.app.domain.Song.class.getName() + ".playlistIds");
            createCache(cm, com.syncify.app.domain.Playlist.class.getName() + ".userIds");
            createCache(cm, com.syncify.app.domain.UserDetails.class.getName() + ".playlistIds");
            // jhipster-needle-caffeine-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

}
