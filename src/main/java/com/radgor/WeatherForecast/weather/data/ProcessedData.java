package com.radgor.WeatherForecast.weather.data;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProcessedData(
        List<String> time,
        List<Integer> weather_code,
        List<Double> temperature_2m_max,
        List<Double> temperature_2m_min,
        List<BigDecimal> generatedEnergy_kWh) {}

