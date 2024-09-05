package com.phoenix_dev.rezervation_management.exception;

/**
 * @author Cankat Sezer
 */

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}