package com.radgor.WeatherForecast.weather.data;

import com.radgor.WeatherForecast.weather.data.hourly.HourlyResponse;
import lombok.Builder;

@Builder
public record WeatherData(DailyResponse daily, HourlyResponse hourly) {
}
