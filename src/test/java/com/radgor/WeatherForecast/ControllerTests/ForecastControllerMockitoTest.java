package com.radgor.WeatherForecast.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radgor.WeatherForecast.WeatherServiceTests.JsonFileReader;
import com.radgor.WeatherForecast.weather.data.WeatherClient;
import com.radgor.WeatherForecast.weather.data.WeatherData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import com.radgor.WeatherForecast.weather.data.summary.SummaryWeatherData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.stream.Stream;

import static com.radgor.WeatherForecast.Utils.RequestUtil.getForecastRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class ForecastControllerMockitoTest {

    private static final String RESPONSE_RESOURCE_PATH = "src/test/java/com/radgor/WeatherForecast/Files/weatherDataResponseFromApi.json";
    private static final String FORECAST_RESOURCE_PATH = "src/test/java/com/radgor/WeatherForecast/Files/forecastResponse.json";
    private static final String SUMMARY_RESOURCE_PATH = "src/test/java/com/radgor/WeatherForecast/Files/summaryResponse.json";

    private static final String FORECAST_URL_PATH = "forecast";
    private static final String SUMMARY_URL_PATH = "summary";

    String HOURLY_PARAMS = "surface_pressure";
    String DAILY_PARAMS = "weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration,precipitation_probability_max";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WeatherClient weatherClient;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("validCoordinatesProvider")
    void happyPathForecastWithMockito(double latitude, double longitude) throws IOException {
        //  given
        var expectedProcessedWeatherDailyData = JsonFileReader.readJson(objectMapper, FORECAST_RESOURCE_PATH, ProcessedWeatherDailyData.class);
        var weatherData = JsonFileReader.readJson(objectMapper, RESPONSE_RESOURCE_PATH, WeatherData.class);

        when(weatherClient.getWeatherData(latitude, longitude, HOURLY_PARAMS, DAILY_PARAMS)).thenReturn(weatherData);

        //  when
        var result = getForecastRequest(webTestClient, FORECAST_URL_PATH, latitude,longitude);

        //  then
        result
                .expectBody(ProcessedWeatherDailyData.class)
                .consumeWith(response -> {
                            assertEquals(expectedProcessedWeatherDailyData, response.getResponseBody());
                            response.getStatus().isSameCodeAs(HttpStatusCode.valueOf(200));
                        }
                );
    }

    @ParameterizedTest
    @MethodSource("validCoordinatesProvider")
    void happyPathSummaryWithMockito(double latitude, double longitude) throws IOException {
        //  given
        var expectedSummaryWeatherData = JsonFileReader.readJson(objectMapper, SUMMARY_RESOURCE_PATH, SummaryWeatherData.class);
        var weatherData = JsonFileReader.readJson(objectMapper, RESPONSE_RESOURCE_PATH, WeatherData.class);

        when(weatherClient.getWeatherData(latitude, longitude, HOURLY_PARAMS, DAILY_PARAMS)).thenReturn(weatherData);

        // when
        var result = getForecastRequest(webTestClient, SUMMARY_URL_PATH, latitude,longitude);

        // then
        result
                .expectBody(SummaryWeatherData.class)
                .consumeWith(response -> {
                            assertEquals(expectedSummaryWeatherData, response.getResponseBody());
                            response.getStatus().isSameCodeAs(HttpStatusCode.valueOf(200));
                        }
                );
    }

    static Stream<Arguments> validCoordinatesProvider() {
        return Stream.of(
                Arguments.of(-90, -180),
                Arguments.of(90, 180),
                Arguments.of(50, 50)
        );
    }
}

