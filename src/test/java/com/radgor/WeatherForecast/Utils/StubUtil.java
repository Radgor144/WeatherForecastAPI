package com.radgor.WeatherForecast.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radgor.WeatherForecast.weather.data.WeatherData;
import com.radgor.WeatherForecast.weather.data.daily.ProcessedWeatherDailyData;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@UtilityClass
public final class StubUtil {

    public static void stubGetWeatherData(ObjectMapper objectMapper, double latitude, double longitude, ProcessedWeatherDailyData weatherData) throws JsonProcessingException {
        String url = "/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&hourly=surface_pressure&daily=weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration,precipitation_probability_max";
        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(weatherData)))
        );
    }


}
