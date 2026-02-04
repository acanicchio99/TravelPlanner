package com.academy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDto {
    private String cityName;
    private String country;
    private Double temperature;
    private String description;
    private Integer humidity;
    private Double windSpeed;
   // private String icon;
}
