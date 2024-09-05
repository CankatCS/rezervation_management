package com.phoenix_dev.rezervation_management.repository;

/**
 * @author Cankat Sezer
 */

import com.phoenix_dev.rezervation_management.model.EmployeeUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeUnavailabilityRepository extends JpaRepository<EmployeeUnavailability, Long> {
    List<EmployeeUnavailability> findByEmployeeId(Long employeeId);

    List<EmployeeUnavailability> findByEmployeeIdAndUnavailableFromBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
}