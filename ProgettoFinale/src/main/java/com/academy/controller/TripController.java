package com.academy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.dto.TripCreateDto;
import com.academy.dto.TripResponseDto;
import com.academy.dto.TripSummaryDto;
import com.academy.dto.TripUpdateDto;
import com.academy.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@Tag(name = "Trip", description = "API per la gestione dei viaggi")
public class TripController {

    private final TripService tripService;

    @PostMapping
    @Operation(summary = "Crea un nuovo viaggio", 
               description = "Crea un nuovo viaggio con nome e intervallo di date")
    public ResponseEntity<TripResponseDto> createTrip(
            @Valid @RequestBody TripCreateDto tripDto) {
        TripResponseDto trip = tripService.createTrip(tripDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(trip);
    }

    @GetMapping
    @Operation(summary = "Visualizza tutti i viaggi", 
               description = "Restituisce un elenco di tutti i viaggi con riepilogo delle tappe")
    public ResponseEntity<List<TripSummaryDto>> getAllTrips() {
        List<TripSummaryDto> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "Visualizza dettagli di un viaggio", 
               description = "Restituisce i dettagli completi di un viaggio con tutte le tappe")
    public ResponseEntity<TripResponseDto> getTripById(
            @Parameter(description = "ID del viaggio")
            @PathVariable Long tripId) {
        TripResponseDto trip = tripService.getTripById(tripId);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{tripId}")
    @Operation(summary = "Modifica un viaggio", 
               description = "Aggiorna le informazioni di un viaggio esistente")
    public ResponseEntity<TripResponseDto> updateTrip(
            @Parameter(description = "ID del viaggio")
            @PathVariable Long tripId,
            @RequestBody TripUpdateDto tripDto) {
        TripResponseDto trip = tripService.updateTrip(tripId, tripDto);
        return ResponseEntity.ok(trip);
    }

    @DeleteMapping("/{tripId}")
    @Operation(summary = "Elimina un viaggio", 
               description = "Elimina un viaggio e tutte le sue tappe in modo permanente")
    public ResponseEntity<Void> deleteTrip(
            @Parameter(description = "ID del viaggio")
            @PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.noContent().build();
    }
}
