package com.radgor.WeatherForecast.weather.processing;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StatisticsCalculator {

    public BigDecimal calculateAverage(List<BigDecimal> values) {
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    public String calculatePrecipitationSummary(List<BigDecimal> precipitationProbabilities) {
        return precipitationProbabilities.stream()
                .anyMatch(precipitation -> precipitation.compareTo(new BigDecimal("50.0")) >= 0)
                ? "Z opadami"
                : "Bez opadów";
    }
}

