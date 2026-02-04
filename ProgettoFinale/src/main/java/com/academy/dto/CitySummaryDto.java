package com.academy.dto;

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
public class CitySummaryDto {
    private Long cityId;
    private String nomeCitta;
    private String nomeRegione;
    private String nomePaese;
}
