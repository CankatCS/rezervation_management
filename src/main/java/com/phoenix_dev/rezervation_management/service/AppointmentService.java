package com.phoenix_dev.rezervation_management.service;

/**
 * @author Cankat Sezer
 */

import com.phoenix_dev.rezervation_management.exception.AppointmentNotFoundException;
import com.phoenix_dev.rezervation_management.model.Appointment;
import com.phoenix_dev.rezervation_management.model.EmployeeUnavailability;
import com.phoenix_dev.rezervation_management.repository.AppointmentRepository;
import com.phoenix_dev.rezervation_management.repository.EmployeeUnavailabilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeUnavailabilityRepository employeeUnavailabilityRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, EmployeeUnavailabilityRepository employeeUnavailabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.employeeUnavailabilityRepository = employeeUnavailabilityRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }

    public List<Appointment> getAppointmentsByCompanyId(Long companyId) {
        return appointmentRepository.findByCompanyId(companyId);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
    }

    public Appointment createAppointment(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxDate = now.plusWeeks(2);

        // Check 2-week window
        if (appointment.getStartTime().isAfter(maxDate)) {
            throw new IllegalArgumentException("Appointment cannot be booked more than 2 weeks in advance.");
        }

        if (!isWithinWorkingHours(appointment)) {
            throw new IllegalArgumentException("Appointment must be within working hours (9 AM to 5 PM).");
        }

        // Check employee unavailability
        if (isEmployeeUnavailable(appointment)) {
            throw new IllegalArgumentException("Employee is unavailable during the requested time.");
        }

        validateAppointmentTimes(appointment);

        if (isOverlapping(appointment, null)) {
            throw new IllegalArgumentException("The appointment time overlaps with an existing appointment.");
        }

        if (appointment.getDurationMinutes() != null && appointment.getStartTime() != null) {
            LocalDateTime endTime = appointment.getStartTime().plus(appointment.getDurationMinutes(), ChronoUnit.MINUTES);
            appointment.setEndTime(endTime);
        } else if (appointment.getEndTime() == null) {
            throw new IllegalArgumentException("End time must be provided if duration is not specified.");
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxDate = now.plusWeeks(2);

        if (updatedAppointment.getStartTime().isAfter(maxDate)) {
            throw new IllegalArgumentException("Appointment cannot be updated to a date more than 2 weeks in advance.");
        }

        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        // Check regular working hours
        if (!isWithinWorkingHours(updatedAppointment)) {
            throw new IllegalArgumentException("Appointment must be within working hours (9 AM to 5 PM).");
        }

        // Check employee unavailability
        if (isEmployeeUnavailable(updatedAppointment)) {
            throw new IllegalArgumentException("Employee is unavailable during the requested time.");
        }

        validateAppointmentTimes(updatedAppointment);

        if (!existingAppointment.getCompany().equals(updatedAppointment.getCompany())) {
            throw new IllegalArgumentException("Cannot change the company of an existing appointment.");
        }

        if (isOverlapping(updatedAppointment, id)) {
            throw new IllegalArgumentException("The appointment time overlaps with an existing appointment.");
        }

        existingAppointment.setStartTime(updatedAppointment.getStartTime());
        existingAppointment.setEndTime(updatedAppointment.getEndTime());
        existingAppointment.setDurationMinutes(updatedAppointment.getDurationMinutes());

        return appointmentRepository.save(existingAppointment);
    }


    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    private void validateAppointmentTimes(Appointment appointment) {
        if (appointment.getStartTime() == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (appointment.getEndTime() != null && appointment.getEndTime().isBefore(appointment.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
    }

    private boolean isOverlapping(Appointment appointment, Long excludeAppointmentId) {
        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                appointment.getCompany(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );

        if (excludeAppointmentId != null) {
            overlappingAppointments = overlappingAppointments.stream()
                    .filter(a -> !a.getId().equals(excludeAppointmentId))
                    .toList();
        }

        return !overlappingAppointments.isEmpty();
    }

    private boolean isWithinWorkingHours(Appointment appointment) {
        LocalTime startWorkHour = appointment.getCompany().getWorkStartTime();
        LocalTime endWorkHour = appointment.getCompany().getWorkEndTime();

        return !appointment.getStartTime().toLocalTime().isBefore(startWorkHour) &&
                !appointment.getEndTime().toLocalTime().isAfter(endWorkHour);
    }

    private boolean isEmployeeUnavailable(Appointment appointment) {
        List<EmployeeUnavailability> unavailabilityList = employeeUnavailabilityRepository.findByEmployeeIdAndUnavailableFromBetween(
                appointment.getEmployee().getId(), appointment.getStartTime(), appointment.getEndTime());

        return !unavailabilityList.isEmpty();  // If the list is not empty, the employee is unavailable
    }


}