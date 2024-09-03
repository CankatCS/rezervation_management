package com.phoenix_dev.rezervation_management.dto;

/**
 * @author Cankat Sezer
 */

public class AuthRequest {
    private String username;
    private String password;

    // Constructors, Getters, and Setters
    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
