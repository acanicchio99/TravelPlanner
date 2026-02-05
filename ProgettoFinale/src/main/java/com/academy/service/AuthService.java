package com.academy.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.academy.dto.AuthResponseDto;
import com.academy.dto.LoginRequestDto;
import com.academy.dto.RegisterRequestDto;
import com.academy.entity.User;
import com.academy.exception.InvalidCredentialsException;
import com.academy.security.CustomUserDetails;
import com.academy.security.JwtService;

import lombok.RequiredArgsConstructor;

/**
 * Service per la gestione dell'autenticazione (login e registrazione).
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Autentica un utente e genera un token JWT
     * 
     * @param loginRequest credenziali di login
     * @return risposta con token JWT e informazioni utente
     * @throws InvalidCredentialsException se le credenziali non sono valide
     */
    public AuthResponseDto authenticate(LoginRequestDto loginRequest) {
        try {
            // Autentica l'utente usando Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Ottiene i dettagli dell'utente autenticato
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Genera il token JWT
            String token = jwtService.generateToken(userDetails);

            // Costruisce la risposta
            return AuthResponseDto.builder()
                    .token(token)
                    .type("Bearer")
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .role(userDetails.getUser().getRole())
                    .build();

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }
    }

    /**
     * Registra un nuovo utente e genera un token JWT
     * 
     * @param registerRequest dati di registrazione
     * @return risposta con token JWT e informazioni utente
     */
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        // Registra l'utente
        User user = userService.registerUser(registerRequest);

        // Crea un oggetto UserDetails per generare il token
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Genera il token JWT
        String token = jwtService.generateToken(userDetails);

        // Costruisce la risposta
        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
