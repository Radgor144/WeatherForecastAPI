package com.radgor.WeatherForecast.weather;

import com.radgor.WeatherForecast.weather.data.CachedWeatherService;
import com.radgor.WeatherForecast.weather.data.WeatherData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedDailyData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import com.radgor.WeatherForecast.weather.data.summary.SummaryWeatherData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final BigDecimal INSTALLATION_POWER_KW = BigDecimal.valueOf(2.5);
    private static final BigDecimal PANEL_EFFICIENCY = BigDecimal.valueOf(0.2);
    private static final BigDecimal SECONDS_IN_AN_HOUR = BigDecimal.valueOf(3600);

    private final CachedWeatherService cachedWeatherService;

    public WeatherService(CachedWeatherService cachedWeatherService) {
        this.cachedWeatherService = cachedWeatherService;
    }

    public ProcessedWeatherDailyData getProcessedWeatherData(double latitude, double longitude) {
        WeatherData weatherData = cachedWeatherService.getWeatherData(latitude, longitude);
        List<BigDecimal> generatedEnergy_kWh = calculateEnergy(weatherData);
        return mapWeatherDataToProcessedWeatherData(weatherData, generatedEnergy_kWh);
    }

    public SummaryWeatherData getSummaryWeatherData(double latitude, double longitude) {
        WeatherData weatherData = cachedWeatherService.getWeatherData(latitude, longitude);
        return mapToSummaryWeatherData(weatherData);
    }

    private List<BigDecimal> calculateEnergy(WeatherData weatherData) {
        List<BigDecimal> generatedEnergy_kWh = new ArrayList<>();

        for (BigDecimal sunshine_duration : weatherData.daily().sunshine_duration()) {
            BigDecimal sunshineHours = sunshine_duration.divide(SECONDS_IN_AN_HOUR, 4, RoundingMode.HALF_UP);
            BigDecimal energy = INSTALLATION_POWER_KW.multiply(sunshineHours).multiply(PANEL_EFFICIENCY);
            generatedEnergy_kWh.add(energy);
        }

        return generatedEnergy_kWh;
    }

    private ProcessedWeatherDailyData mapWeatherDataToProcessedWeatherData(WeatherData weatherData, List<BigDecimal> generatedEnergy_kWh) {

        return ProcessedWeatherDailyData.builder()
                .proccessedData(
                        ProcessedDailyData.builder()
                                .time(weatherData.daily().time())
                                .weather_code(weatherData.daily().weather_code())
                                .temperature_2m_max(weatherData.daily().temperature_2m_max())
                                .temperature_2m_min(weatherData.daily().temperature_2m_min())
                                .generatedEnergy_kWh(generatedEnergy_kWh)  // Używamy List<BigDecimal>
                                .build()
                )
                .build();
    }

    private SummaryWeatherData mapToSummaryWeatherData(WeatherData weatherData) {

        List<BigDecimal> hourlyPressures = weatherData.hourly().surface_pressure();
        List<BigDecimal> sunshineDuration = weatherData.daily().sunshine_duration();
        List<BigDecimal> temperatureMax = weatherData.daily().temperature_2m_max();
        List<BigDecimal> temperatureMin = weatherData.daily().temperature_2m_min();
        List<BigDecimal> precipitationProbabilities = weatherData.daily().precipitation_probability_max();

        BigDecimal avgPressure = calculateAveragePressure(hourlyPressures);
        BigDecimal avgSunshineDuration = calculateAverageSunshineDuration(sunshineDuration);
        BigDecimal avgTempMax = calculateAverageTemperature(temperatureMax);
        BigDecimal avgTempMin = calculateAverageTemperature(temperatureMin);

        String precipitationSummary = calculatePrecipitationSummary(precipitationProbabilities);

        return new SummaryWeatherData(
                avgPressure,
                avgSunshineDuration,
                avgTempMax,
                avgTempMin,
                precipitationSummary
        );
    }

    private BigDecimal calculateAveragePressure(List<BigDecimal> pressures) {
        BigDecimal totalPressure = BigDecimal.ZERO;
        for (BigDecimal pressure : pressures) {
            totalPressure = totalPressure.add(pressure);
        }
        return totalPressure.divide(BigDecimal.valueOf(pressures.size()), RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAverageSunshineDuration(List<BigDecimal> sunshineDurations) {
        BigDecimal totalSunshine = BigDecimal.ZERO;
        for (BigDecimal sunshine : sunshineDurations) {
            totalSunshine = totalSunshine.add(sunshine);
        }
        return totalSunshine.divide(BigDecimal.valueOf(sunshineDurations.size()), RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAverageTemperature(List<BigDecimal> temperatures) {
        BigDecimal totalTemperature = BigDecimal.ZERO;

        for (BigDecimal temp : temperatures) {
            totalTemperature = totalTemperature.add(temp);
        }

        BigDecimal averageTemperature = totalTemperature.divide(BigDecimal.valueOf(temperatures.size()), RoundingMode.HALF_UP);
        return averageTemperature.setScale(2, RoundingMode.HALF_UP);
    }

    private String calculatePrecipitationSummary(List<BigDecimal> precipitationProbabilities) {
        return precipitationProbabilities.stream()
                .anyMatch(precipitation -> precipitation.compareTo(new BigDecimal("50.0")) >= 0)
                ? "Z opadami"
                : "Bez opadów";
    }
}
