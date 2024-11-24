package com.radgor.WeatherForecast.weather.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<ErrorResponse> handleCustomHttpException(CustomHttpException ex) {
        log.error("Error while connecting to weather api", ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse("Error while connecting to weather client API.", ex.getMessage(), ex.getHttpStatus()));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Error while user entered bad arguments", ex);
        return new ErrorResponse("Error while user entered bad arguments", ex.getMessage(), 400);
    }
}
