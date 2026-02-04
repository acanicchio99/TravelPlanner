package com.academy.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
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
public class TripStopCreateDto {

    @NotNull(message = "City ID is required")
    private Long cityId;

    @NotNull(message = "Visit date is required")
    private LocalDate visitDate;

    private String notes;
    private Integer orderInTrip;
}
