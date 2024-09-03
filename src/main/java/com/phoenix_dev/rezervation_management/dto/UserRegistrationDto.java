package com.phoenix_dev.rezervation_management.dto;

import com.phoenix_dev.rezervation_management.model.Role;

import java.util.Set;

/**
 * @author Cankat Sezer
 */
public class UserRegistrationDto {
    private String username;
    private String password;
    private String email;
    private Set<Role> roles;

    public UserRegistrationDto() {}

    public UserRegistrationDto(String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}