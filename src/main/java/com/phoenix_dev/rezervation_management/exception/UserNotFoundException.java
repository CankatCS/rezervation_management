package com.phoenix_dev.rezervation_management.exception;

/**
 * @author Cankat Sezer
 */

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}