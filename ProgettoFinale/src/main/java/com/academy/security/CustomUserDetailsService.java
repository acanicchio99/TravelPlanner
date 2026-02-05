package com.academy.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.academy.entity.User;
import com.academy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementazione personalizzata di UserDetailsService.
 * Carica i dettagli dell'utente dal database per l'autenticazione.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username: " + username));
        
        return new CustomUserDetails(user);
    }
}
