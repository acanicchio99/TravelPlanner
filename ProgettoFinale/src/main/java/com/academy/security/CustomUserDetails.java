package com.academy.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.academy.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implementazione personalizzata di UserDetails che wrappa l'entità User.
 * Fornisce le informazioni necessarie a Spring Security per l'autenticazione e l'autorizzazione.
 */
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte il ruolo dell'utente in un'autorità Spring Security
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    /**
     * Ottiene l'email dell'utente
     */
    public String getEmail() {
        return user.getEmail();
    }

    /**
     * Ottiene l'ID dell'utente
     */
    public Long getId() {
        return user.getId();
    }
}
