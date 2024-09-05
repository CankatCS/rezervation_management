package com.phoenix_dev.rezervation_management.exception;

/**
 * @author Cankat Sezer
 */
public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
