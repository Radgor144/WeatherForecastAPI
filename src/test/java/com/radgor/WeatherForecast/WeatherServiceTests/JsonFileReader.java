package com.radgor.WeatherForecast.WeatherServiceTests;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public final class JsonFileReader {

    private JsonFileReader() {
    }

    public static <T> T readJson(ObjectMapper objectMapper, String pathToFile, Class<T> resourceType) throws IOException {
        return objectMapper.readValue(new File(pathToFile), resourceType);
    }

}
