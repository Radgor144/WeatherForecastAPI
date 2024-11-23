package com.radgor.WeatherForecast.weather.data.daily;

import lombok.Builder;

@Builder
public record ProcessedWeatherDailyData(ProcessedDailyData proccessedData) {
}
