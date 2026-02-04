package com.academy.dto;

import java.time.LocalDate;

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
public class TripStopResponseDto {

    private Long stopId;
    private Long cityId;
    private String nomeCitta;
    private String nomeRegione;
    private String nomePaese;
    private LocalDate visitDate;
    private String notes;
    private Integer orderInTrip;
}
