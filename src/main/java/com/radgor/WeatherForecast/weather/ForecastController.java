package com.radgor.WeatherForecast.weather;

import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import com.radgor.WeatherForecast.weather.data.summary.SummaryWeatherData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class ForecastController {

    private final WeatherService weatherService;

    @GetMapping("/forecast")
    public ProcessedWeatherDailyData getWeatherData(
            @RequestParam @Valid @Min(-90) @Max(90) double latitude,
            @RequestParam @Valid @Min(-180) @Max(180) double longitude) {
        return weatherService.getProcessedWeatherData(latitude, longitude);
    }

    @GetMapping("/summary")
    public SummaryWeatherData getSummaryWeatherData(
            @RequestParam @Valid @Min(-90) @Max(90) double latitude,
            @RequestParam @Valid @Min(-180) @Max(180) double longitude) {
        return weatherService.getSummaryWeatherData(latitude, longitude);
    }
}