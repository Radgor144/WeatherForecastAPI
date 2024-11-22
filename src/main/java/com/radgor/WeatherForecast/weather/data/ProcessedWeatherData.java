package com.radgor.WeatherForecast.weather.data;

import lombok.Builder;

@Builder
public record ProcessedWeatherData(ProcessedData proccessedData) {
}
