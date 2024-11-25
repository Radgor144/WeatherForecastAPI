package com.radgor.WeatherForecast.Utils;

import lombok.experimental.UtilityClass;
import org.springframework.test.web.reactive.server.WebTestClient;

@UtilityClass
public class RequestUtil {
    public static WebTestClient.ResponseSpec getForecastRequest(WebTestClient webTestClient, String path, double latitude, double longitude) {
        return webTestClient
                .get()
                .uri("/" + path + "?latitude=" + latitude + "&longitude=" + longitude)
                .exchange();
    }

}
