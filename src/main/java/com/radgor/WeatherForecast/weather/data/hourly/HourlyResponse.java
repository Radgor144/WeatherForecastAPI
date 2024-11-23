package com.radgor.WeatherForecast.weather.data.hourly;

import java.math.BigDecimal;
import java.util.List;

public record HourlyResponse(List<String> time, List<BigDecimal> surface_pressure) {
}
