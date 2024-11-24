package com.radgor.WeatherForecast.weather.exceptions;

public class WeatherDataNotFoundException extends RuntimeException {

    public WeatherDataNotFoundException(String message) {
        super(message);
    }
}
