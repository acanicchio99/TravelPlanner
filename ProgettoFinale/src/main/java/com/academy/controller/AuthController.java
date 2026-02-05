package com.academy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.dto.AuthResponseDto;
import com.academy.dto.LoginRequestDto;
import com.academy.dto.RegisterRequestDto;
import com.academy.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller per la gestione dell'autenticazione (registrazione e login).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticazione", description = "API per la gestione dell'autenticazione utenti")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint per la registrazione di un nuovo utente
     * 
     * @param registerRequest dati di registrazione
     * @return risposta con token JWT e informazioni utente
     */
    @PostMapping("/register")
    @Operation(
        summary = "Registrazione nuovo utente",
        description = "Crea un nuovo account utente nel sistema. Richiede username unico, email valida e password sicura (minimo 8 caratteri). Restituisce un token JWT per l'accesso immediato."
    )
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        AuthResponseDto response = authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per il login di un utente esistente
     * 
     * @param loginRequest credenziali di login
     * @return risposta con token JWT e informazioni utente
     */
    @PostMapping("/login")
    @Operation(
        summary = "Login utente",
        description = "Autentica un utente registrato tramite username e password. Restituisce un token JWT che deve essere incluso nell'header Authorization (Bearer token) per accedere agli endpoint protetti."
    )
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto response = authService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }
}
