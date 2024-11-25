//package com.radgor.WeatherForecast;
//
//import com.radgor.WeatherForecast.weather.exceptions.ErrorResponse;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import java.util.stream.Stream;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
//import static com.radgor.WeatherForecast.Utils.RequestUtil.getForecastRequest;
//
//@SpringBootTest(properties =
//        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWireMock(port = 0)
//@AutoConfigureWebTestClient
//public class ErrorTest {
//
//    public static final String URL = "/v1/forecast?latitude=56.8&longitude=33.5&hourly=surface_pressure&daily=weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration,precipitation_probability_max";
//    public static final String ERROR_MESSAGE = "Error while connecting to weather client API.";
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @ParameterizedTest
//    @MethodSource("shouldMapExternalErrorCodeToInternalErrorCodeMethodSource")
//    void shouldMapExternalErrorCodeToInternalErrorCode(String reason, int httpCode, int expectedHttpCode) {
//        //given
//        stubFor(get(urlEqualTo(URL))
//                .willReturn(aResponse()
//                        .withStatus(httpCode)
//                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//                        .withBody(reason))
//        );
//
//        //when
//        var result = getForecastRequest(webTestClient, 56.8, 33.5);
//
//        //then
//        result
//                .expectStatus().isEqualTo(expectedHttpCode)
//                .expectBody(ErrorResponse.class)
//                .isEqualTo(new ErrorResponse(ERROR_MESSAGE, reason, expectedHttpCode));
//    }
//
//    private static Stream<Arguments> shouldMapExternalErrorCodeToInternalErrorCodeMethodSource() {
//        return Stream.of(
//                Arguments.of("Bad API Request:Invalid location parameter value.", 400, 400),
//                Arguments.of("No account found with API key 'fake_api_key'", 401, 401),
//                Arguments.of("Not found weather data", 404, 404),
//                Arguments.of("Too many requests", 429, 429),
//                Arguments.of("Bad gateway", 502, 502),
//                Arguments.of("Unexpected external error 502", 418, 502)
//        );
//    }
//}
