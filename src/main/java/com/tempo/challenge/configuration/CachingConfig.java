package com.tempo.challenge.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CachingConfig {
    @Value("${app.cache.ttl}")
    private Integer cacheTtl;

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(cacheTtl, TimeUnit.SECONDS).maximumSize(100);
    }

    @Bean
    public CacheManager cacheManager(final Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("fee");
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
