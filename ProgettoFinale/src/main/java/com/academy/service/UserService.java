package com.academy.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academy.dto.RegisterRequestDto;
import com.academy.dto.UpdateProfileDto;
import com.academy.dto.UserProfileDto;
import com.academy.entity.Role;
import com.academy.entity.User;
import com.academy.entity.UserProfile;
import com.academy.exception.DuplicateResourceException;
import com.academy.exception.ResourceNotFoundException;
import com.academy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service per la gestione degli utenti e dei profili.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuovo utente nel sistema
     * 
     * @param registerDto dati di registrazione
     * @return utente creato
     * @throws DuplicateResourceException se username o email già esistono
     */
    @Transactional
    public User registerUser(RegisterRequestDto registerDto) {
        // Verifica che username e email non siano già in uso
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new DuplicateResourceException("Utente", "username", registerDto.getUsername());
        }
        
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new DuplicateResourceException("Utente", "email", registerDto.getEmail());
        }

        // Crea l'entità User
        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        // Crea il profilo associato
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .build();

        user.setUserProfile(userProfile);

        // Salva l'utente (il profilo viene salvato in cascata)
        return userRepository.save(user);
    }

    /**
     * Recupera il profilo completo di un utente
     * 
     * @param username username dell'utente
     * @return DTO con i dati del profilo
     * @throws ResourceNotFoundException se l'utente non esiste
     */
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "username", username));

        return convertToProfileDto(user);
    }

    /**
     * Aggiorna il profilo di un utente
     * 
     * @param username username dell'utente
     * @param updateDto dati da aggiornare
     * @return DTO con i dati aggiornati
     * @throws ResourceNotFoundException se l'utente non esiste
     * @throws DuplicateResourceException se l'email è già in uso da un altro utente
     */
    @Transactional
    public UserProfileDto updateUserProfile(String username, UpdateProfileDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "username", username));

        // Verifica che la nuova email non sia già in uso da un altro utente
        if (!user.getEmail().equals(updateDto.getEmail()) && 
            userRepository.existsByEmail(updateDto.getEmail())) {
            throw new DuplicateResourceException("Utente", "email", updateDto.getEmail());
        }

        // Aggiorna i dati dell'utente
        user.setEmail(updateDto.getEmail());
        
        // Aggiorna il profilo
        UserProfile userProfile = user.getUserProfile();
        if (userProfile == null) {
            userProfile = UserProfile.builder()
                    .user(user)
                    .firstName(updateDto.getFirstName())
                    .lastName(updateDto.getLastName())
                    .build();
            user.setUserProfile(userProfile);
        } else {
            userProfile.setFirstName(updateDto.getFirstName());
            userProfile.setLastName(updateDto.getLastName());
        }

        User updatedUser = userRepository.save(user);
        return convertToProfileDto(updatedUser);
    }

    /**
     * Converte un'entità User in UserProfileDto
     */
    private UserProfileDto convertToProfileDto(User user) {
        UserProfile profile = user.getUserProfile();
        
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(profile != null ? profile.getFirstName() : null)
                .lastName(profile != null ? profile.getLastName() : null)
                .role(user.getRole())
                .build();
    }
}
