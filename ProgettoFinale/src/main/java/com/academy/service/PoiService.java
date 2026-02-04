package com.academy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.academy.dto.PoiDto;
import com.academy.entity.City;
import com.academy.repository.CityRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PoiService {

    private final CityRepository cityRepository;
    
    @Value("${geoapify.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int RADIUS_METERS = 5000;
    private static final String GEOAPIFY_API_URL = "https://api.geoapify.com/v2/places?categories={categories}&filter=circle:{lon},{lat},{radius}&bias=proximity:{lon},{lat}&limit=20&apiKey={apiKey}";

    @SuppressWarnings("UseSpecificCatch")
    public List<PoiDto> getPointsOfInterest(Long cityId) {
        City city = cityRepository.findById(cityId)
            .orElseThrow(() -> new RuntimeException("City not found with id: " + cityId));
        
        if (city.getLatitude() == null || city.getLongitude() == null) {
            throw new RuntimeException("City coordinates not available");
        }

        if (apiKey == null || apiKey.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            String categories = "tourism.sights,tourism.attraction,entertainment.museum,natural.water,natural.mountain,beach";
            
            String response = restTemplate.getForObject(
                GEOAPIFY_API_URL,
                String.class,
                categories,
                city.getLongitude(),
                city.getLatitude(),
                RADIUS_METERS,
                city.getLongitude(),
                city.getLatitude(),
                apiKey
            );

            return parseGeoapifyResponse(response);

        } catch (Exception e) {
            System.err.println("Error fetching POIs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private List<PoiDto> parseGeoapifyResponse(String jsonResponse) {
        List<PoiDto> pois = new ArrayList<>();
        
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode features = root.path("features");
            
            if (features.isArray()) {
                for (JsonNode feature : features) {
                    JsonNode properties = feature.path("properties");
                    
                    if (!properties.isMissingNode() && !properties.path("name").asText().isEmpty()) {
                        JsonNode categories = properties.path("categories");
                        String category = "unknown";
                        if (categories.isArray() && categories.size() > 0) {
                            category = categories.get(0).asText();
                        }
                        
                        PoiDto poi = PoiDto.builder()
                            .name(properties.path("name").asText())
                            .category(category)
                            .latitude(properties.path("lat").asDouble())
                            .longitude(properties.path("lon").asDouble())
                            .address(properties.path("formatted").asText(null))
                            .distance(properties.path("distance").asInt(0))
                            .build();
                        
                        pois.add(poi);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing Geoapify response: " + e.getMessage());
        }
        
        return pois;
    }
}
