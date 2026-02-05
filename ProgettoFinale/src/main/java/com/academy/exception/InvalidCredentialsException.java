package com.academy.exception;

/**
 * Eccezione lanciata quando le credenziali di autenticazione non sono valide.
 */
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public InvalidCredentialsException() {
        super("Username o password non validi");
    }
}
