package com.academy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.academy.dto.WeatherDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${openweather.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}&units=metric&lang=it";

    @SuppressWarnings("UseSpecificCatch")
    public WeatherDto getWeather(Double latitude, Double longitude) {
        if (apiKey == null || apiKey.isEmpty()) {
            return WeatherDto.builder()
                    .description("API Key non configurata")
                    .build();
        }

        try {
            String response = restTemplate.getForObject(
                    WEATHER_API_URL,
                    String.class,
                    latitude,
                    longitude,
                    apiKey
            );

            JsonNode root = objectMapper.readTree(response);
            JsonNode main = root.path("main");
            JsonNode weather = root.path("weather").get(0);
            JsonNode wind = root.path("wind");

            return WeatherDto.builder()
                    .cityName(root.path("name").asText())
                    .country(root.path("sys").path("country").asText())
                    .temperature(main.path("temp").asDouble())
                    .description(weather.path("description").asText())
                    .humidity(main.path("humidity").asInt())
                    .windSpeed(wind.path("speed").asDouble())
                    //.icon(weather.path("icon").asText())
                    .build();

        } catch (Exception e) {
            return WeatherDto.builder()
                    .description("Errore nel recupero dati meteo: " + e.getMessage())
                    .build();
        }
    }
}
