package com.radgor.WeatherForecast.weather.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class RetryerConfig{
    @Bean
    public Retryer retryer(){
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(3L), 5);
    }
}
