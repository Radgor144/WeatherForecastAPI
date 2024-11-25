package com.radgor.WeatherForecast.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radgor.WeatherForecast.Utils.StubUtil;
import com.radgor.WeatherForecast.WeatherServiceTests.JsonFileReader;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.stream.Stream;

import static com.radgor.WeatherForecast.Utils.RequestUtil.getForecastRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
public class ControllerTest {

    private static final String FORECAST_RESOURCE_PATH = "src/test/java/com/radgor/WeatherForecast/Files/forecastResponse.json";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;


    @ParameterizedTest
    @MethodSource("validCoordinatesProvider")
    public void happyPath(double latitude, double longitude) throws IOException {
        // given
        ProcessedWeatherDailyData expectedProcessedWeatherDailyData = JsonFileReader.readJson(objectMapper, FORECAST_RESOURCE_PATH, ProcessedWeatherDailyData.class);
        System.out.println("XXX");
        System.out.println(expectedProcessedWeatherDailyData);

        StubUtil.stubGetWeatherData(objectMapper, latitude, longitude, expectedProcessedWeatherDailyData);

        // when
        var result = getForecastRequest(webTestClient, latitude, longitude);

        // then
                result
                .expectBody(ProcessedWeatherDailyData.class)
                .consumeWith(response -> {
                            assertEquals(expectedProcessedWeatherDailyData, response.getResponseBody());
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
