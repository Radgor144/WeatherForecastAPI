package com.radgor.WeatherForecast.weather.data.daily;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProcessedDailyData(
        List<String> time,
        List<Integer> weather_code,
        List<BigDecimal> temperature_2m_max,
        List<BigDecimal> temperature_2m_min,
        List<BigDecimal> generatedEnergy_kWh) {}

