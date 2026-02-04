package com.academy.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class PoiDto {
    private String name;
    private String category;
    
    @JsonIgnore
    private Double latitude;
    
    @JsonIgnore
    private Double longitude;
    
    private String address;
    private Integer distance;
}
