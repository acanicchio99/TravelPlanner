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

import com.academy.dto.TripStopCreateDto;
import com.academy.dto.TripStopResponseDto;
import com.academy.dto.TripStopUpdateDto;
import com.academy.service.TripStopService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips/{tripId}/stops")
@RequiredArgsConstructor
@Tag(name = "TripStop", description = "API per la gestione delle tappe di un viaggio")
public class TripStopController {

    private final TripStopService tripStopService;

    @PostMapping
    @Operation(summary = "Aggiungi una tappa al viaggio", 
               description = "Aggiunge una nuova tappa a un viaggio esistente. La data della tappa deve essere compresa nell'intervallo del viaggio.")
    public ResponseEntity<TripStopResponseDto> addStop(
            @Parameter(description = "ID del viaggio")
            @PathVariable Long tripId,
            @Valid @RequestBody TripStopCreateDto stopDto) {
        TripStopResponseDto stop = tripStopService.addStopToTrip(tripId, stopDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(stop);
    }

    @GetMapping
    @Operation(summary = "Visualizza tutte le tappe del viaggio", 
               description = "Restituisce l'elenco di tutte le tappe di un viaggio ordinato per orderInTrip")
    public ResponseEntity<List<TripStopResponseDto>> getAllStops(
            @Parameter(description = "ID del viaggio")
            @PathVariable Long tripId) {
        List<TripStopResponseDto> stops = tripStopService.getStopsByTrip(tripId);
        return ResponseEntity.ok(stops);
    }

    @PutMapping("/{stopId}")
    @Operation(summary = "Modifica una tappa", 
               description = "Aggiorna le informazioni di una tappa esistente (città, data, note, ordine)")
    public ResponseEntity<TripStopResponseDto> updateStop(
            @Parameter(description = "ID del viaggio")
            @PathVariable Long tripId,
            @Parameter(description = "ID della tappa")
            @PathVariable Long stopId,
            @RequestBody TripStopUpdateDto stopDto) {
        TripStopResponseDto stop = tripStopService.updateStop(tripId, stopId, stopDto);
        return ResponseEntity.ok(stop);
    }

    @DeleteMapping("/{stopId}")
    @Operation(summary = "Rimuovi una tappa", 
               description = "Rimuove una tappa dal viaggio in modo permanente")
    public ResponseEntity<Void> deleteStop(
            @Parameter(description = "ID del viaggio")
            @PathVariable Long tripId,
            @Parameter(description = "ID della tappa")
            @PathVariable Long stopId) {
        tripStopService.deleteStop(tripId, stopId);
        return ResponseEntity.noContent().build();
    }
}
