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
public class TripSummaryDto {

    private Long tripId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfStops;
    private String firstDestination;
}
