package com.radgor.WeatherForecast.weather.data;

import com.radgor.WeatherForecast.weather.config.RetryerConfig;
import com.radgor.WeatherForecast.weather.config.WeatherClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-client", configuration = {WeatherClientConfiguration.class, RetryerConfig.class})
public interface WeatherClient {

    @GetMapping("/v1/forecast")
    WeatherData getWeatherData(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("hourly") String hourly,
            @RequestParam("daily") String daily
    );
}
