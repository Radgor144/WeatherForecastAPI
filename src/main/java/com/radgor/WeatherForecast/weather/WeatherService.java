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
import java.util.stream.Collectors;


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

    private List<BigDecimal> calculateAvgPressure(WeatherData weatherData) {
        List<BigDecimal> hourlyPressures = weatherData.hourly().surface_pressure();
        List<BigDecimal> avgPressures = new ArrayList<>();

        BigDecimal sum = BigDecimal.ZERO;
        int hoursInDay = 24;
        int counter = 0;

        for (int i = 0; i < hourlyPressures.size(); i++) {
            BigDecimal hourlyPressure = hourlyPressures.get(i);
            sum = sum.add(hourlyPressure);
            counter++;

            if (counter == hoursInDay) {
                BigDecimal avg = sum.divide(BigDecimal.valueOf(hoursInDay), RoundingMode.HALF_UP);
                avgPressures.add(avg);

                sum = BigDecimal.ZERO;
                counter = 0;
            }
        }

        if (counter > 0) {
            BigDecimal avg = sum.divide(BigDecimal.valueOf(counter), RoundingMode.HALF_UP);
            avgPressures.add(avg);
        }

        return avgPressures;
    }


    private SummaryWeatherData mapToSummaryWeatherData(WeatherData weatherData) {

        List<BigDecimal> avgPressure = calculateAvgPressure(weatherData);
        List<BigDecimal> sunshineDuration = weatherData.daily().sunshine_duration();
        List<Double> temperatureMax = weatherData.daily().temperature_2m_max();
        List<Double> temperatureMin = weatherData.daily().temperature_2m_min();
        List<String> precipitationSummary = weatherData.daily().precipitation_probability_max().stream()
                .map(precipitation -> precipitation.compareTo(new BigDecimal("50.0")) >= 0 ? "z opadami" : "bez opadów")
                .collect(Collectors.toList());

        return new SummaryWeatherData(
                avgPressure,
                sunshineDuration,
                temperatureMax,
                temperatureMin,
                precipitationSummary
        );
    }

    private List<BigDecimal> calculateEnergy(WeatherData weatherData) {
        List<BigDecimal> generatedEnergy_kWh = new ArrayList<>();

        for (BigDecimal sunshine_duration : weatherData.daily().sunshine_duration()) {
            BigDecimal sunshineHours = sunshine_duration.divide(SECONDS_IN_AN_HOUR, 4, RoundingMode.HALF_UP); // Sekundy na godziny
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
                                .generatedEnergy_kWh(generatedEnergy_kWh)
                                .build()
                )
                .build();
    }
}
