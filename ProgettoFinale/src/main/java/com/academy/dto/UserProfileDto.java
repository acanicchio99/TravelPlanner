package com.academy.dto;

import com.academy.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO per la risposta del profilo utente completo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    
    private Long id;
    
    private String username;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private Role role;
}
