package com.academy.util;

import java.time.LocalDate;

import com.academy.entity.Trip;

public class TripValidationUtils {

    public static void validateStopDateInTripRange(LocalDate visitDate, Trip trip) {
        if (visitDate.isBefore(trip.getStartDate()) || visitDate.isAfter(trip.getEndDate())) {
            throw new IllegalArgumentException(
                    String.format("Visit date must be between %s and %s",
                            trip.getStartDate(), trip.getEndDate()));
        }
    }

    private TripValidationUtils() {
        // Utility class - prevent instantiation
    }
}
