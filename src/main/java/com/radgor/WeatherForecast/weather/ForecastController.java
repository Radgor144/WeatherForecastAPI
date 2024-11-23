package com.radgor.WeatherForecast.weather;

import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import com.radgor.WeatherForecast.weather.data.summary.SummaryWeatherData;
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

    @GetMapping("/forecast")
    public ProcessedWeatherDailyData getWeatherData(@RequestParam double latitude, @RequestParam double longitude) {
        return weatherService.getProcessedWeatherData(latitude, longitude);
    }

    @GetMapping("/summary")
    public SummaryWeatherData getSummaryWeatherData(@RequestParam double latitude, @RequestParam double longitude) {
        return weatherService.getSummaryWeatherData(latitude, longitude);
    }

}