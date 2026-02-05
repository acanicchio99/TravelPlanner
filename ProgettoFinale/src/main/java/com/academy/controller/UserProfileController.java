package com.academy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.dto.UpdateProfileDto;
import com.academy.dto.UserProfileDto;
import com.academy.security.CustomUserDetails;
import com.academy.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller per la gestione del profilo utente.
 * Tutti gli endpoint richiedono autenticazione JWT.
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profilo Utente", description = "API per la gestione del profilo utente autenticato")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserService userService;

    /**
     * Endpoint per recuperare il profilo dell'utente autenticato
     * 
     * @param userDetails dettagli dell'utente autenticato
     * @return profilo utente
     */
    @GetMapping
    @Operation(
        summary = "Visualizza profilo",
        description = "Recupera i dati del profilo dell'utente autenticato, inclusi username, email, nome e cognome. Richiede un token JWT valido nell'header Authorization."
    )
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        UserProfileDto profile = userService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }

    /**
     * Endpoint per aggiornare il profilo dell'utente autenticato
     * 
     * @param userDetails dettagli dell'utente autenticato
     * @param updateDto dati da aggiornare
     * @return profilo aggiornato
     */
    @PutMapping
    @Operation(
        summary = "Aggiorna profilo",
        description = "Aggiorna i dati del profilo dell'utente autenticato (email, nome, cognome). L'email deve essere unica nel sistema. Richiede un token JWT valido nell'header Authorization."
    )
    public ResponseEntity<UserProfileDto> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProfileDto updateDto) {
        
        String username = userDetails.getUsername();
        UserProfileDto updatedProfile = userService.updateUserProfile(username, updateDto);
        return ResponseEntity.ok(updatedProfile);
    }
}
