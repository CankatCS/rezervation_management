package com.phoenix_dev.rezervation_management.service;

/**
 * @author Cankat Sezer
 */

import com.phoenix_dev.rezervation_management.exception.AppointmentNotFoundException;
import com.phoenix_dev.rezervation_management.model.Appointment;
import com.phoenix_dev.rezervation_management.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
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
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

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
}