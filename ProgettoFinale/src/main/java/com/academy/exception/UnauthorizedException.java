package com.academy.exception;

/**
 * Eccezione lanciata quando un utente tenta di accedere a una risorsa senza permessi adeguati.
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException() {
        super("Non hai i permessi per accedere a questa risorsa");
    }
}
