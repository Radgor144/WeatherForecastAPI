package com.radgor.WeatherForecast.weather.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class WeatherClientCacheConfig {

    public static final String CACHENAME = "WeatherData";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHENAME);
    }

    @CacheEvict(allEntries = true, value = {CACHENAME})
    @Scheduled(fixedDelay = 1 * 60 * 1000) // refresh cache every 10 minutes
    public void evictCache() {
        log.info("Refresh Cache");
    }


}

