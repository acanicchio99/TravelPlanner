package com.academy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academy.dto.TripStopCreateDto;
import com.academy.dto.TripStopResponseDto;
import com.academy.dto.TripStopUpdateDto;
import com.academy.entity.City;
import com.academy.entity.Trip;
import com.academy.entity.TripStop;
import com.academy.exception.ResourceNotFoundException;
import com.academy.exception.UnauthorizedException;
import com.academy.repository.CityRepository;
import com.academy.repository.TripRepository;
import com.academy.repository.TripStopRepository;
import com.academy.util.TripValidationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripStopService {

    private final TripStopRepository tripStopRepository;
    private final TripRepository tripRepository;
    private final CityRepository cityRepository;

    @Transactional
    public TripStopResponseDto addStopToTrip(Long tripId, TripStopCreateDto stopDto, String username) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "tripId", tripId));
        
        // Verifica che il trip appartenga all'utente autenticato
        validateTripOwnership(trip, username);

        // Validate visit date is within trip date range
        TripValidationUtils.validateStopDateInTripRange(stopDto.getVisitDate(), trip);

        City city = cityRepository.findById(stopDto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("Città", "cityId", stopDto.getCityId()));

        TripStop tripStop = TripStop.builder()
                .trip(trip)
                .city(city)
                .visitDate(stopDto.getVisitDate())
                .notes(stopDto.getNotes())
                .orderInTrip(stopDto.getOrderInTrip())
                .build();

        TripStop savedStop = tripStopRepository.save(tripStop);
        return convertToResponseDto(savedStop);
    }

    @Transactional(readOnly = true)
    public List<TripStopResponseDto> getStopsByTrip(Long tripId, String username) {
        // Verify trip exists
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "tripId", tripId));
        
        // Verifica che il trip appartenga all'utente autenticato
        validateTripOwnership(trip, username);

        List<TripStop> stops = tripStopRepository.findByTripTripIdOrderByOrderInTripAsc(tripId);
        return stops.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripStopResponseDto updateStop(Long tripId, Long stopId, TripStopUpdateDto stopDto, String username) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "tripId", tripId));
        
        // Verifica che il trip appartenga all'utente autenticato
        validateTripOwnership(trip, username);

        TripStop stop = tripStopRepository.findById(stopId)
                .orElseThrow(() -> new ResourceNotFoundException("TripStop", "stopId", stopId));

        // Verify stop belongs to the trip
        if (!stop.getTrip().getTripId().equals(tripId)) {
            throw new IllegalArgumentException("La tappa non appartiene a questo viaggio");
        }

        if (stopDto.getCityId() != null) {
            City city = cityRepository.findById(stopDto.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Città", "cityId", stopDto.getCityId()));
            stop.setCity(city);
        }

        if (stopDto.getVisitDate() != null) {
            TripValidationUtils.validateStopDateInTripRange(stopDto.getVisitDate(), trip);
            stop.setVisitDate(stopDto.getVisitDate());
        }

        if (stopDto.getNotes() != null) {
            stop.setNotes(stopDto.getNotes());
        }

        if (stopDto.getOrderInTrip() != null) {
            stop.setOrderInTrip(stopDto.getOrderInTrip());
        }

        TripStop updatedStop = tripStopRepository.save(stop);
        return convertToResponseDto(updatedStop);
    }

    @Transactional
    public void deleteStop(Long tripId, Long stopId, String username) {
        // Verify trip exists
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "tripId", tripId));
        
        // Verifica che il trip appartenga all'utente autenticato
        validateTripOwnership(trip, username);

        TripStop stop = tripStopRepository.findById(stopId)
                .orElseThrow(() -> new ResourceNotFoundException("TripStop", "stopId", stopId));

        // Verify stop belongs to the trip
        if (!stop.getTrip().getTripId().equals(tripId)) {
            throw new IllegalArgumentException("La tappa non appartiene a questo viaggio");
        }

        tripStopRepository.delete(stop);
    }
    
    /**
     * Verifica che il trip appartenga all'utente specificato
     */
    private void validateTripOwnership(Trip trip, String username) {
        if (trip.getUser() == null || !trip.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Non hai i permessi per accedere a questo viaggio");
        }
    }

    private TripStopResponseDto convertToResponseDto(TripStop stop) {
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
}
