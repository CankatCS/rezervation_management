package com.phoenix_dev.rezervation_management.repository;

/**
 * @author Cankat Sezer
 */

import com.phoenix_dev.rezervation_management.model.Appointment;
import com.phoenix_dev.rezervation_management.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.company = :company AND " +
            "((:startTime < a.endTime) AND (:endTime > a.startTime))")
    List<Appointment> findOverlappingAppointments(
            @Param("company") Company company,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    List<Appointment> findByUserId(Long userId);

    List<Appointment> findByCompanyId(Long companyId);
}
