package com.radgor.WeatherForecast.weather.processing;

import com.radgor.WeatherForecast.weather.data.WeatherData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedDailyData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import com.radgor.WeatherForecast.weather.data.summary.SummaryWeatherData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WeatherDataMapper {

    public ProcessedWeatherDailyData mapToProcessedWeatherData(WeatherData weatherData, List<BigDecimal> generatedEnergy_kWh) {
        return ProcessedWeatherDailyData.builder()
                .proccessedData(
                        ProcessedDailyData.builder()
                                .time(weatherData.daily().time())
                                .weather_code(weatherData.daily().weather_code())
                                .temperature_2m_max(weatherData.daily().temperature_2m_max())
                                .temperature_2m_min(weatherData.daily().temperature_2m_min())
                                .generatedEnergy_kWh(generatedEnergy_kWh)
                                .build()
                )
                .build();
    }

    public SummaryWeatherData mapToSummaryWeatherData(WeatherData weatherData, BigDecimal avgPressure, BigDecimal avgSunshineDuration,
                                                      BigDecimal avgTempMax, BigDecimal avgTempMin, String precipitationSummary) {
        return new SummaryWeatherData(avgPressure, avgSunshineDuration, avgTempMax, avgTempMin, precipitationSummary);
    }
}

