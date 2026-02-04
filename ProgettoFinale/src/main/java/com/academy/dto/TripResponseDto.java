package com.academy.dto;

import java.time.LocalDate;
import java.util.List;

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
public class TripResponseDto {

    private Long tripId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private List<TripStopResponseDto> stops;
}
