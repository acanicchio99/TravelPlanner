package com.academy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO per l'aggiornamento del profilo utente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileDto {
    
    @NotBlank(message = "Email è obbligatoria")
    @Email(message = "Email deve essere valida")
    private String email;
    
    @NotBlank(message = "Nome è obbligatorio")
    @Size(min = 2, max = 50, message = "Nome deve essere tra 2 e 50 caratteri")
    private String firstName;
    
    @NotBlank(message = "Cognome è obbligatorio")
    @Size(min = 2, max = 50, message = "Cognome deve essere tra 2 e 50 caratteri")
    private String lastName;
}
