package com.radgor.WeatherForecast.weather.data;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.radgor.WeatherForecast.weather.config.WeatherClientCacheConfig.CACHENAME;

@Service
public class CachedWeatherService {
    private final WeatherClient weatherClient;

    public CachedWeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    @Cacheable(cacheNames = CACHENAME)
    public WeatherData getWeatherData(double latitude, double longitude) {
        String dailyParams = "weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration,precipitation_probability_max";
        String hourlyParams = "surface_pressure";
        System.out.println("Pobieranie danych z API...");
        return weatherClient.getWeatherData(latitude, longitude, hourlyParams, dailyParams);
    }

}

