package com.radgor.WeatherForecast.weather.data.summary;

import java.math.BigDecimal;
import java.util.List;

public record SummaryWeatherData(
        List<BigDecimal> avgPressure,
        List<BigDecimal> sunshine_duration,
        List<Double> temperature_2m_max,
        List<Double> temperature_2m_min,
        List<String> precipitationSummary
) {
}
