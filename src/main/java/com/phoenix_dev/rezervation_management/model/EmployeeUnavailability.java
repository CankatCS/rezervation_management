package com.phoenix_dev.rezervation_management.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * @author Cankat Sezer
 */
@Entity
public class EmployeeUnavailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private User employee;

    private LocalDateTime unavailableFrom;
    private LocalDateTime unavailableUntil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public LocalDateTime getUnavailableFrom() {
        return unavailableFrom;
    }

    public void setUnavailableFrom(LocalDateTime unavailableFrom) {
        this.unavailableFrom = unavailableFrom;
    }

    public LocalDateTime getUnavailableUntil() {
        return unavailableUntil;
    }

    public void setUnavailableUntil(LocalDateTime unavailableUntil) {
        this.unavailableUntil = unavailableUntil;
    }
}

