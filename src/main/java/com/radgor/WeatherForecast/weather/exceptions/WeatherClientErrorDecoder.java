package com.radgor.WeatherForecast.weather.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@RequiredArgsConstructor
public class WeatherClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        final String message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            message  = new Scanner(bodyIs).useDelimiter("\\Z").next();
        } catch (IOException e) {
            return new CustomHttpException("Error while parsing response: %s".formatted(e.getMessage()), 500);
        }
        return switch (response.status()) {
            case 400 -> new CustomHttpException(message != null ? message : "Bad Request", 400);
            case 401 -> new CustomHttpException(message != null ? message : "Invalid api key", 401);
            case 404 -> new CustomHttpException(message != null ? message : "Not found weather data", 404);
            case 429 -> new CustomHttpException(message != null ? message : "Too many requests", 429);
            case 500 -> new CustomHttpException(message != null ? message : "Bad gateway", 502);
            default -> new CustomHttpException(message != null ? message : "Unexpected external error %s".formatted(response.status()), 502);
        };
    }
}
