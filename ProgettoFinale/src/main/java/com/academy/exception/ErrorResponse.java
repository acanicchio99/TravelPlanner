package com.academy.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO per rappresentare le risposte di errore strutturate dell'API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    /**
     * Timestamp dell'errore
     */
    private LocalDateTime timestamp;
    
    /**
     * Codice di stato HTTP
     */
    private int status;
    
    /**
     * Messaggio di errore principale
     */
    private String message;
    
    /**
     * Percorso della richiesta che ha generato l'errore
     */
    private String path;
    
    /**
     * Lista di errori di validazione (opzionale)
     */
    private List<String> errors;
    
    /**
     * Costruttore semplificato senza lista di errori
     */
    public ErrorResponse(LocalDateTime timestamp, int status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
