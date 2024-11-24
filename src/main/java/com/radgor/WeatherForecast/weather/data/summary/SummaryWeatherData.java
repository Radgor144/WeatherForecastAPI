package com.radgor.WeatherForecast.weather.data.summary;

import java.math.BigDecimal;

public record SummaryWeatherData(
        BigDecimal avgPressure,
        BigDecimal sunshine_duration,
        BigDecimal temperature_2m_max,
        BigDecimal temperature_2m_min,
        String precipitationSummary
) {
}
