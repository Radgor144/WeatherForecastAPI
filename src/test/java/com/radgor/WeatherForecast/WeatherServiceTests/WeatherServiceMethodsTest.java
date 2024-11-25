package com.radgor.WeatherForecast.WeatherServiceTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radgor.WeatherForecast.weather.WeatherService;
import com.radgor.WeatherForecast.weather.data.CachedWeatherService;
import com.radgor.WeatherForecast.weather.data.WeatherData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import com.radgor.WeatherForecast.weather.data.summary.SummaryWeatherData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherServiceMethodsTest {

    private static final String RESPONSE_RESOURCE_PATH = "src/test/java/com/radgor/WeatherForecast/Files/weatherDataResponseFromApi.json";
    private static final String FORECAST_RESOURCE_PATH = "src/test/java/com/radgor/WeatherForecast/Files/forecastResponse.json";
    private static final String SUMMARY_RESOURCE_PATH = "src/test/java/com/radgor/WeatherForecast/Files/summaryResponse.json";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private CachedWeatherService cachedWeatherService;

    @ParameterizedTest
    @MethodSource("validCoordinatesProvider")
    public void shouldReturnProcessedWeatherDataForValidCoordinates(double latitude, double longitude) throws IOException {
        // given
        WeatherData expectedWeatherData = JsonFileReader.readJson(objectMapper, RESPONSE_RESOURCE_PATH, WeatherData.class);
        ProcessedWeatherDailyData expectedProcessedWeatherDailyData = JsonFileReader.readJson(objectMapper, FORECAST_RESOURCE_PATH, ProcessedWeatherDailyData.class);

        when(cachedWeatherService.getWeatherData(latitude, longitude)).thenReturn(expectedWeatherData);

        // when
        var result = weatherService.getProcessedWeatherData(latitude, longitude);

        // then
        assertEquals(expectedProcessedWeatherDailyData, result);
    }

    @ParameterizedTest
    @MethodSource("validCoordinatesProvider")
    public void shouldReturnSummaryWeatherDataForValidCoordinates(double latitude, double longitude) throws IOException {
        // given
        WeatherData expectedWeatherData = JsonFileReader.readJson(objectMapper, RESPONSE_RESOURCE_PATH, WeatherData.class);
        SummaryWeatherData expectedSummaryWeatherData = JsonFileReader.readJson(objectMapper, SUMMARY_RESOURCE_PATH, SummaryWeatherData.class);

        when(cachedWeatherService.getWeatherData(latitude, longitude)).thenReturn(expectedWeatherData);

        // when
        var result = weatherService.getSummaryWeatherData(latitude, longitude);

        // then
        assertEquals(expectedSummaryWeatherData, result);
    }

    static Stream<Arguments> validCoordinatesProvider() {
        return Stream.of(
                Arguments.of(-90, -180),
                Arguments.of(90, 180),
                Arguments.of(50, 50)
        );
    }
}