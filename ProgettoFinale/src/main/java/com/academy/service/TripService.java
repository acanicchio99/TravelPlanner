package com.academy.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academy.dto.TripCreateDto;
import com.academy.dto.TripResponseDto;
import com.academy.dto.TripStopResponseDto;
import com.academy.dto.TripSummaryDto;
import com.academy.dto.TripUpdateDto;
import com.academy.entity.Trip;
import com.academy.entity.TripStop;
import com.academy.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    @Transactional
    public TripResponseDto createTrip(TripCreateDto tripDto) {
        // Validate dates
        if (tripDto.getEndDate().isBefore(tripDto.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        Trip trip = Trip.builder()
                .name(tripDto.getName())
                .startDate(tripDto.getStartDate())
                .endDate(tripDto.getEndDate())
                .description(tripDto.getDescription())
                .build();

        Trip savedTrip = tripRepository.save(trip);
        return convertToResponseDto(savedTrip);
    }

    @Transactional(readOnly = true)
    public List<TripSummaryDto> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripResponseDto getTripById(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        return convertToResponseDto(trip);
    }

    @Transactional
    public TripResponseDto updateTrip(Long tripId, TripUpdateDto tripDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (tripDto.getName() != null) {
            trip.setName(tripDto.getName());
        }
        if (tripDto.getStartDate() != null) {
            trip.setStartDate(tripDto.getStartDate());
        }
        if (tripDto.getEndDate() != null) {
            trip.setEndDate(tripDto.getEndDate());
        }
        if (tripDto.getDescription() != null) {
            trip.setDescription(tripDto.getDescription());
        }

        // Validate dates after update
        if (trip.getEndDate().isBefore(trip.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        Trip updatedTrip = tripRepository.save(trip);
        return convertToResponseDto(updatedTrip);
    }

    @Transactional
    public void deleteTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        tripRepository.delete(trip);
    }

    private TripResponseDto convertToResponseDto(Trip trip) {
        List<TripStopResponseDto> stops = trip.getTripStops().stream()
                .sorted(Comparator.comparing(TripStop::getOrderInTrip, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::convertStopToDto)
                .collect(Collectors.toList());

        return TripResponseDto.builder()
                .tripId(trip.getTripId())
                .name(trip.getName())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .description(trip.getDescription())
                .stops(stops)
                .build();
    }

    private TripStopResponseDto convertStopToDto(TripStop stop) {
        return TripStopResponseDto.builder()
                .stopId(stop.getStopId())
                .cityId(stop.getCity().getCityId())
                .nomeCitta(stop.getCity().getNome())
                .nomeRegione(stop.getCity().getRegion().getNome())
                .nomePaese(stop.getCity().getRegion().getCountry().getNome())
                .visitDate(stop.getVisitDate())
                .notes(stop.getNotes())
                .orderInTrip(stop.getOrderInTrip())
                .build();
    }

    private TripSummaryDto convertToSummaryDto(Trip trip) {
        String firstDestination = trip.getTripStops().stream()
                .sorted(Comparator.comparing(TripStop::getOrderInTrip, Comparator.nullsLast(Comparator.naturalOrder())))
                .findFirst()
                .map(stop -> stop.getCity().getNome())
                .orElse("No destinations yet");

        return TripSummaryDto.builder()
                .tripId(trip.getTripId())
                .name(trip.getName())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .numberOfStops(trip.getTripStops().size())
                .firstDestination(firstDestination)
                .build();
    }
}
