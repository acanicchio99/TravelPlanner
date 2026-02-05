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
import com.academy.entity.User;
import com.academy.exception.ResourceNotFoundException;
import com.academy.exception.UnauthorizedException;
import com.academy.repository.TripRepository;
import com.academy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Transactional
    public TripResponseDto createTrip(TripCreateDto tripDto, String username) {
        // Validate dates
        if (tripDto.getEndDate().isBefore(tripDto.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        // Recupera l'utente autenticato
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "username", username));

        Trip trip = Trip.builder()
                .name(tripDto.getName())
                .startDate(tripDto.getStartDate())
                .endDate(tripDto.getEndDate())
                .description(tripDto.getDescription())
                .user(user)
                .build();

        Trip savedTrip = tripRepository.save(trip);
        return convertToResponseDto(savedTrip);
    }

    @Transactional(readOnly = true)
    public List<TripSummaryDto> getAllTrips(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "username", username));
        
        List<Trip> trips = tripRepository.findAll().stream()
                .filter(trip -> trip.getUser() != null && trip.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
        
        return trips.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripResponseDto getTripById(Long tripId, String username) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "tripId", tripId));
        
        // Verifica che il trip appartenga all'utente autenticato
        validateTripOwnership(trip, username);
        
        return convertToResponseDto(trip);
    }

    @Transactional
    public TripResponseDto updateTrip(Long tripId, TripUpdateDto tripDto, String username) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "tripId", tripId));
        
        // Verifica che il trip appartenga all'utente autenticato
        validateTripOwnership(trip, username);

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
    public void deleteTrip(Long tripId, String username) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "tripId", tripId));
        
        // Verifica che il trip appartenga all'utente autenticato
        validateTripOwnership(trip, username);
        
        tripRepository.delete(trip);
    }
    
    /**
     * Verifica che il trip appartenga all'utente specificato
     */
    private void validateTripOwnership(Trip trip, String username) {
        if (trip.getUser() == null || !trip.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Non hai i permessi per accedere a questo viaggio");
        }
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
