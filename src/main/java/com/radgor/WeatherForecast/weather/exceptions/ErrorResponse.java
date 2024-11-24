package com.radgor.WeatherForecast.weather.exceptions;

public record ErrorResponse(String message, String reason, int codeHTTP) {

}
