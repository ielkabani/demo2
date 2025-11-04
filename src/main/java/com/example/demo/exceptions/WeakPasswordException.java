package com.example.demo.exceptions;

/**
 * Exception thrown when password validation fails
 */
public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException(String message) {
        super(message);
    }
}

