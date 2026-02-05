package com.academy.dto;

import com.academy.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO per la risposta di autenticazione (login o registrazione).
 * Contiene il token JWT e le informazioni base dell'utente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    
    /**
     * Token JWT per l'autenticazione
     */
    private String token;
    
    /**
     * Tipo di token (sempre "Bearer")
     */
    @Builder.Default
    private String type = "Bearer";
    
    /**
     * Username dell'utente autenticato
     */
    private String username;
    
    /**
     * Email dell'utente autenticato
     */
    private String email;
    
    /**
     * Ruolo dell'utente
     */
    private Role role;
}
