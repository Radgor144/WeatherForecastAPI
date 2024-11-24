package com.radgor.WeatherForecast.weather.config;

import com.radgor.WeatherForecast.weather.exceptions.WeatherClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class WeatherClientConfiguration {
    @Bean
    ErrorDecoder errorDecoder() {
        return new WeatherClientErrorDecoder();
    }
}
