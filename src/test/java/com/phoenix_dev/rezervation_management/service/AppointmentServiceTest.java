package com.phoenix_dev.rezervation_management.service;

import com.phoenix_dev.rezervation_management.model.Appointment;
import com.phoenix_dev.rezervation_management.model.Company;
import com.phoenix_dev.rezervation_management.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(appointmentService.getAllAppointments().isEmpty());
    }

    @Test
    void testGetAppointmentsByUserId() {
        when(appointmentRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        assertTrue(appointmentService.getAppointmentsByUserId(1L).isEmpty());
    }

    @Test
    void testGetAppointmentsByCompanyId() {
        when(appointmentRepository.findByCompanyId(1L)).thenReturn(Collections.emptyList());
        assertTrue(appointmentService.getAppointmentsByCompanyId(1L).isEmpty());
    }

    @Test
    void testGetAppointmentById() {
        Appointment appointment = new Appointment();
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        assertEquals(appointment, appointmentService.getAppointmentById(1L));
    }

    @Test
    void testCreateAppointment() {
        Appointment appointment = new Appointment();
        appointment.setStartTime(LocalDateTime.now());
        appointment.setDurationMinutes(60);

        when(appointmentRepository.findOverlappingAppointments(any(), any(), any())).thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        assertEquals(appointment, appointmentService.createAppointment(appointment));
    }

    @Test
    void testUpdateAppointment() {
        Company company = new Company();
        company.setCompanyName("CompanyA");

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(1L);
        existingAppointment.setCompany(company);

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setStartTime(LocalDateTime.now());
        updatedAppointment.setEndTime(LocalDateTime.now().plusHours(1));
        updatedAppointment.setCompany(company);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.findOverlappingAppointments(any(), any(), any())).thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedAppointment);

        assertEquals(updatedAppointment, appointmentService.updateAppointment(1L, updatedAppointment));
    }


    @Test
    void testDeleteAppointment() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(appointmentRepository).deleteById(1L);

        appointmentService.deleteAppointment(1L);
        verify(appointmentRepository, times(1)).deleteById(1L);
    }
}