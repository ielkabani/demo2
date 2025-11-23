package com.example.demo.exceptions;

/**
 * Exception thrown when trying to perform an invalid state transition
 */
public class InvalidUserStateException extends RuntimeException {
    public InvalidUserStateException(String message) {
        super(message);
    }
}

