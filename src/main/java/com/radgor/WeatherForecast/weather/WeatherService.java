package com.radgor.WeatherForecast.weather;

import com.radgor.WeatherForecast.weather.data.ProcessedData;
import com.radgor.WeatherForecast.weather.data.ProcessedWeatherData;
import com.radgor.WeatherForecast.weather.data.WeatherClient;
import com.radgor.WeatherForecast.weather.data.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WeatherService {

    private static final BigDecimal INSTALLATION_POWER_KW = BigDecimal.valueOf(2.5);
    private static final BigDecimal PANEL_EFFICIENCY = BigDecimal.valueOf(0.2);
    private static final BigDecimal SECONDS_IN_AN_HOUR = BigDecimal.valueOf(3600);

    private final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public ProcessedWeatherData getWeatherData(double latitude, double longitude) {
        String dailyParams = "weather_code,temperature_2m_max,temperature_2m_min,daylight_duration";
        WeatherData weatherData = weatherClient.getWeatherData(latitude, longitude, dailyParams);

        return calculateEnergyAndMapWeatherData(weatherData);
    }

    private ProcessedWeatherData calculateEnergyAndMapWeatherData(WeatherData weatherData) {
        List<BigDecimal> generatedEnergy_kWh = new ArrayList<>();

        for (BigDecimal daylightDuration : weatherData.daily().daylight_duration()) {
            BigDecimal daylightHours = daylightDuration.divide(SECONDS_IN_AN_HOUR, 4, RoundingMode.HALF_UP); // Sekundy na godziny
            BigDecimal energy = INSTALLATION_POWER_KW.multiply(daylightHours).multiply(PANEL_EFFICIENCY);
            generatedEnergy_kWh.add(energy);
        }

        return mapWeatherDataToProcessedWeatherData(weatherData, generatedEnergy_kWh);
    }

    private ProcessedWeatherData mapWeatherDataToProcessedWeatherData(WeatherData weatherData, List<BigDecimal> generatedEnergy_kWh) {
        return ProcessedWeatherData.builder()
                .proccessedData(
                        ProcessedData.builder()
                                .time(weatherData.daily().time())
                                .weather_code(weatherData.daily().weather_code())
                                .temperature_2m_max(weatherData.daily().temperature_2m_max())
                                .temperature_2m_min(weatherData.daily().temperature_2m_min())
                                .generatedEnergy_kWh(generatedEnergy_kWh)
                                .build()
                )
                .build();
    }
}
