package com.academy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.dto.SearchHistoryDto;
import com.academy.service.SearchHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search-history")
@RequiredArgsConstructor
@Tag(name = "Search History", description = "API per visualizzare la cronologia delle ricerche")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping
    @Operation(summary = "Visualizza cronologia ricerche", 
               description = "Restituisce tutte le ricerche effettuate dagli utenti con data, termine cercato e numero risultati")
    public ResponseEntity<List<SearchHistoryDto>> getAllSearchHistory() {
        List<SearchHistoryDto> history = searchHistoryService.getAllSearchHistory();
        return ResponseEntity.ok(history);
    }
}
