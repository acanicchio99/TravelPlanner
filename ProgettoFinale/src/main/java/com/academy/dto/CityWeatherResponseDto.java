package com.academy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityWeatherResponseDto {
    private Long cityId;
    private String nomeCitta;
    private String nomeRegione;
    private String nomePaese;
    private Double latitude;
    private Double longitude;
    private WeatherDto weather;
}
