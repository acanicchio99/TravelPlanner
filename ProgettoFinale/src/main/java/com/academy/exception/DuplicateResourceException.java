package com.academy.exception;

/**
 * Eccezione lanciata quando si tenta di creare una risorsa che già esiste.
 */
public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s già esistente con %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
