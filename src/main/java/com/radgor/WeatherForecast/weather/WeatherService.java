package com.radgor.WeatherForecast.weather;

import com.radgor.WeatherForecast.weather.data.CachedWeatherService;
import com.radgor.WeatherForecast.weather.data.WeatherData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import com.radgor.WeatherForecast.weather.data.summary.SummaryWeatherData;
import com.radgor.WeatherForecast.weather.processing.EnergyCalculator;
import com.radgor.WeatherForecast.weather.processing.StatisticsCalculator;
import com.radgor.WeatherForecast.weather.processing.WeatherDataMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WeatherService {

    private final CachedWeatherService cachedWeatherService;
    private final EnergyCalculator energyCalculator;
    private final WeatherDataMapper weatherDataMapper;
    private final StatisticsCalculator statisticsCalculator;

    public WeatherService(
            CachedWeatherService cachedWeatherService,
            EnergyCalculator energyCalculator,
            WeatherDataMapper weatherDataMapper,
            StatisticsCalculator statisticsCalculator
    ) {
        this.cachedWeatherService = cachedWeatherService;
        this.energyCalculator = energyCalculator;
        this.weatherDataMapper = weatherDataMapper;
        this.statisticsCalculator = statisticsCalculator;
    }

    public ProcessedWeatherDailyData getProcessedWeatherData(double latitude, double longitude) {
        WeatherData weatherData = cachedWeatherService.getWeatherData(latitude, longitude);

        List<BigDecimal> generatedEnergy_kWh = energyCalculator.calculateEnergy(weatherData.daily().sunshine_duration());

        return weatherDataMapper.mapToProcessedWeatherData(weatherData, generatedEnergy_kWh);
    }

    public SummaryWeatherData getSummaryWeatherData(double latitude, double longitude) {
        WeatherData weatherData = cachedWeatherService.getWeatherData(latitude, longitude);

        BigDecimal avgPressure = statisticsCalculator.calculateAverage(weatherData.hourly().surface_pressure());
        BigDecimal avgSunshineDuration = statisticsCalculator.calculateAverage(weatherData.daily().sunshine_duration());
        BigDecimal avgTempMax = statisticsCalculator.calculateAverage(weatherData.daily().temperature_2m_max());
        BigDecimal avgTempMin = statisticsCalculator.calculateAverage(weatherData.daily().temperature_2m_min());
        String precipitationSummary = statisticsCalculator.calculatePrecipitationSummary(weatherData.daily().precipitation_probability_max());

        return weatherDataMapper.mapToSummaryWeatherData(weatherData, avgPressure, avgSunshineDuration, avgTempMax, avgTempMin, precipitationSummary);
    }
}
