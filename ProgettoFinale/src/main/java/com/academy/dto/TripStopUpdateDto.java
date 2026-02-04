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
public class TripStopUpdateDto {

    private Long cityId;
    private LocalDate visitDate;
    private String notes;
    private Integer orderInTrip;
}
