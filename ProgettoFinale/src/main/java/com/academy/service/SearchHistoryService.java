package com.academy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academy.dto.SearchHistoryDto;
import com.academy.entity.SearchHistory;
import com.academy.repository.SearchHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional(readOnly = true)
    public List<SearchHistoryDto> getAllSearchHistory() {
        List<SearchHistory> history = searchHistoryRepository.findAll();
        return history.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SearchHistoryDto convertToDto(SearchHistory searchHistory) {
        return SearchHistoryDto.builder()
                .id(searchHistory.getId())
                .searchTerm(searchHistory.getSearchTerm())
                .searchedAt(searchHistory.getSearchedAt())
                .resultsCount(searchHistory.getResultsCount())
                .build();
    }
}
