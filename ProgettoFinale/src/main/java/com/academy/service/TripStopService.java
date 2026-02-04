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
    public TripStopResponseDto addStopToTrip(Long tripId, TripStopCreateDto stopDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // Validate visit date is within trip date range
        TripValidationUtils.validateStopDateInTripRange(stopDto.getVisitDate(), trip);

        City city = cityRepository.findById(stopDto.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found: " + stopDto.getCityId()));

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
    public List<TripStopResponseDto> getStopsByTrip(Long tripId) {
        // Verify trip exists
        tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        List<TripStop> stops = tripStopRepository.findByTripTripIdOrderByOrderInTripAsc(tripId);
        return stops.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripStopResponseDto updateStop(Long tripId, Long stopId, TripStopUpdateDto stopDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        TripStop stop = tripStopRepository.findById(stopId)
                .orElseThrow(() -> new RuntimeException("Stop not found: " + stopId));

        // Verify stop belongs to the trip
        if (!stop.getTrip().getTripId().equals(tripId)) {
            throw new RuntimeException("Stop does not belong to this trip");
        }

        if (stopDto.getCityId() != null) {
            City city = cityRepository.findById(stopDto.getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found: " + stopDto.getCityId()));
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
    public void deleteStop(Long tripId, Long stopId) {
        // Verify trip exists
        tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        TripStop stop = tripStopRepository.findById(stopId)
                .orElseThrow(() -> new RuntimeException("Stop not found: " + stopId));

        // Verify stop belongs to the trip
        if (!stop.getTrip().getTripId().equals(tripId)) {
            throw new RuntimeException("Stop does not belong to this trip");
        }

        tripStopRepository.delete(stop);
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
