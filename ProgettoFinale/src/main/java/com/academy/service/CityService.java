package com.academy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academy.dto.CityResponseDto;
import com.academy.dto.CitySummaryDto;
import com.academy.dto.TagDto;
import com.academy.dto.WeatherDto;
import com.academy.entity.City;
import com.academy.entity.SearchHistory;
import com.academy.repository.CityRepository;
import com.academy.repository.SearchHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final WeatherService weatherService;

    @Transactional
    public List<CityResponseDto> searchCitiesByName(String name) {
        // Cerca le città
        List<City> cities = cityRepository.findByNomeContainingIgnoreCase(name);
        
        // Salva nella search history
        SearchHistory searchHistory = SearchHistory.builder()
                .searchTerm(name)
                .resultsCount(cities.size())
                .build();
        searchHistoryRepository.save(searchHistory);
        
        // Converti in DTO
        return cities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitySummaryDto> filterCitiesByTagId(Long tagId) {
        List<City> cities = cityRepository.findByTagId(tagId);
        return cities.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WeatherDto getWeatherByCity(Long cityId) {
        City city = cityRepository.findById(cityId).orElse(null);
        
        if (city == null) {
            return null;
        }
        
        // Verifica che la città abbia coordinate valide
        if (city.getLatitude() == null || city.getLongitude() == null) {
            return WeatherDto.builder()
                    .description("Coordinate non disponibili per questa città")
                    .build();
        }
        
        return weatherService.getWeather(city.getLatitude(), city.getLongitude());
    }

    private CityResponseDto convertToDto(City city) {
        List<TagDto> tagDtos = city.getTags().stream()
                .map(tag -> TagDto.builder()
                        .tagId(tag.getTagId())
                        .nome(tag.getNome())
                        .descrizione(tag.getDescrizione())
                        .build())
                .collect(Collectors.toList());

        return CityResponseDto.builder()
                .cityId(city.getCityId())
                .nomeCitta(city.getNome())
                .regioneNome(city.getRegion().getNome())
                .paeseNome(city.getRegion().getCountry().getNome())
                .tags(tagDtos)
                .build();
    }

    private CitySummaryDto convertToSummaryDto(City city) {
        return CitySummaryDto.builder()
                .cityId(city.getCityId())
                .nomeCitta(city.getNome())
                .nomeRegione(city.getRegion().getNome())
                .nomePaese(city.getRegion().getCountry().getNome())
                .build();
    }
}
