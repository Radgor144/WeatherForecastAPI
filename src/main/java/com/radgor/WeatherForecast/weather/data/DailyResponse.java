package com.radgor.WeatherForecast.weather.data;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record DailyResponse(
        List<String> time,
        List<Integer> weather_code,
        List<BigDecimal> temperature_2m_max,
        List<BigDecimal> temperature_2m_min,
        List<BigDecimal> sunshine_duration,
        List<BigDecimal> precipitation_probability_max) {}
