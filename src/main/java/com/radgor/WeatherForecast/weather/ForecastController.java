package com.radgor.WeatherForecast.weather;

import com.radgor.WeatherForecast.weather.data.ProcessedWeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final WeatherService weatherService;

    // da sie znalezc miasto po kodzie pocztowym!
//    @Cacheable(cacheNames = CACHENAME)
    @GetMapping("/forecast")
    public ProcessedWeatherData getWeatherData(@RequestParam double latitude, @RequestParam double longitude) {
//        double latitude = 52.52;
//        double longitude = 13.41;

        return weatherService.getWeatherData(latitude, longitude);
    }

}