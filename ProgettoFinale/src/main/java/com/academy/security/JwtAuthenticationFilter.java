package com.academy.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro JWT per l'autenticazione delle richieste.
 * Estrae il token JWT dall'header Authorization, lo valida e imposta il contesto di sicurezza.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Estrae l'header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Verifica se l'header contiene un token Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrae il token (rimuove "Bearer " dall'inizio)
        jwt = authHeader.substring(7);

        try {
            // Estrae lo username dal token
            username = jwtService.extractUsername(jwt);

            // Se lo username è valido e non c'è già un'autenticazione nel contesto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carica i dettagli dell'utente
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Valida il token
                if (jwtService.validateToken(jwt, userDetails)) {
                    // Crea l'oggetto di autenticazione
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Imposta i dettagli aggiuntivi dalla richiesta
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Imposta l'autenticazione nel contesto di sicurezza
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log dell'errore (in produzione usare un logger appropriato)
            logger.error("Errore durante l'autenticazione JWT: " + e.getMessage());
        }

        // Continua con la catena dei filtri
        filterChain.doFilter(request, response);
    }
}
