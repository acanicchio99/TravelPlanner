package com.academy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO per la richiesta di login.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    
    @NotBlank(message = "Username è obbligatorio")
    private String username;
    
    @NotBlank(message = "Password è obbligatoria")
    private String password;
}
