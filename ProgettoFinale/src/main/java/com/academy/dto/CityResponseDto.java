package com.academy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponseDto {
    private Long cityId;
    private String nomeCitta;
    private String regioneNome;
    private String paeseNome;
    private List<TagDto> tags;
}
