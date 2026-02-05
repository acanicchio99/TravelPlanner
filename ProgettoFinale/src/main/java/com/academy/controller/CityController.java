package com.academy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academy.dto.CityResponseDto;
import com.academy.dto.CitySummaryDto;
import com.academy.dto.PoiDto;
import com.academy.dto.WeatherDto;
import com.academy.service.CityService;
import com.academy.service.PoiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
@Tag(name = "City", description = "API per la gestione e ricerca delle città")
@SecurityRequirement(name = "bearerAuth")
public class CityController {

    private final CityService cityService;
    private final PoiService poiService;

    @GetMapping("/search")
    @Operation(summary = "Ricerca città per nome", 
               description = "Cerca città il cui nome contiene il termine specificato. Restituisce nome città, regione, paese e tag. Registra la ricerca nella search_history per consentire la successiva selezione.")
    public ResponseEntity<List<CityResponseDto>> searchCities(
            @Parameter(description = "Nome o parte del nome della città da cercare")
            @RequestParam String name) {
        List<CityResponseDto> cities = cityService.searchCitiesByName(name);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/filter-by-tag-id")
    @Operation(summary = "Filtra città per ID tag", 
               description = "Restituisce solo nome città, regione e paese delle città associate al tag specificato tramite ID")
    public ResponseEntity<List<CitySummaryDto>> filterByTagId(
            @Parameter(description = "ID del tag")
            @RequestParam Long tagId) {
        List<CitySummaryDto> cities = cityService.filterCitiesByTagId(tagId);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{cityId}/weather")
    @Operation(summary = "Visualizza meteo per città selezionata", 
               description = "Dopo aver selezionato una città dalla ricerca, ottiene i dati meteo correnti da OpenWeatherMap usando l'ID della città")
    public ResponseEntity<WeatherDto> getWeatherByCity(
            @Parameter(description = "ID della città selezionata dalla ricerca")
            @PathVariable Long cityId) {
        WeatherDto weather = cityService.getWeatherByCity(cityId);
        if (weather == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/{cityId}/pois")
    @Operation(summary = "Ottieni punti di interesse per città selezionata", 
               description = "Dopo aver selezionato una città dalla ricerca, ottiene i punti di interesse (natural, tourism, beach) in un raggio di 5km usando l'API Geoapify")
    public ResponseEntity<List<PoiDto>> getPointsOfInterest(
            @Parameter(description = "ID della città selezionata dalla ricerca")
            @PathVariable Long cityId) {
        List<PoiDto> pois = poiService.getPointsOfInterest(cityId);
        if (pois == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pois);
    }
}
