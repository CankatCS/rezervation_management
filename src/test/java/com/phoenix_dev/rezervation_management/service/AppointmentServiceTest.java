package com.phoenix_dev.rezervation_management.service;

import com.phoenix_dev.rezervation_management.model.Appointment;
import com.phoenix_dev.rezervation_management.model.Company;
import com.phoenix_dev.rezervation_management.model.User;
import com.phoenix_dev.rezervation_management.repository.AppointmentRepository;
import com.phoenix_dev.rezervation_management.repository.EmployeeUnavailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmployeeUnavailabilityRepository employeeUnavailabilityRepository;

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
        Company company = new Company();
        company.setWorkStartTime(LocalTime.of(9, 0));
        company.setWorkEndTime(LocalTime.of(17, 0));

        User employee = new User();
        employee.setId(1L);
        employee.setCompany(company);

        Appointment appointment = new Appointment();
        appointment.setStartTime(LocalDateTime.now().plusDays(5).withHour(10));
        appointment.setDurationMinutes(60);
        appointment.setEndTime(appointment.getStartTime().plusMinutes(60));
        appointment.setCompany(company);
        appointment.setEmployee(employee);

        when(appointmentRepository.findOverlappingAppointments(any(), any(), any())).thenReturn(Collections.emptyList());
        when(employeeUnavailabilityRepository.findByEmployeeIdAndUnavailableFromBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        assertDoesNotThrow(() -> appointmentService.createAppointment(appointment));
    }

    @Test
    void testUpdateAppointment() {
        Company company = new Company();
        company.setCompanyName("CompanyA");
        company.setWorkStartTime(LocalTime.of(9, 0));
        company.setWorkEndTime(LocalTime.of(17, 0));

        User employee = new User();
        employee.setId(1L);
        employee.setCompany(company);

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(1L);
        existingAppointment.setCompany(company);
        existingAppointment.setEmployee(employee);

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setStartTime(LocalDateTime.now().plusDays(5).withHour(10));
        updatedAppointment.setEndTime(updatedAppointment.getStartTime().plusHours(1));
        updatedAppointment.setCompany(company);
        updatedAppointment.setEmployee(employee);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.findOverlappingAppointments(any(), any(), any())).thenReturn(Collections.emptyList());
        when(employeeUnavailabilityRepository.findByEmployeeIdAndUnavailableFromBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedAppointment);

        assertDoesNotThrow(() -> appointmentService.updateAppointment(1L, updatedAppointment));
    }

    @Test
    void testDeleteAppointment() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(appointmentRepository).deleteById(1L);
        appointmentService.deleteAppointment(1L);
        verify(appointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateAppointmentBeyondTwoWeeks() {
        Appointment appointment = new Appointment();
        appointment.setStartTime(LocalDateTime.now().plusWeeks(3));
        assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointment));
    }

    @Test
    void testCreateAppointmentWithinTwoWeeks() {
        Company company = new Company();
        company.setWorkStartTime(LocalTime.of(9, 0));
        company.setWorkEndTime(LocalTime.of(17, 0));

        User employee = new User();
        employee.setId(1L);
        employee.setCompany(company);

        Appointment appointment = new Appointment();
        appointment.setStartTime(LocalDateTime.now().plusDays(10).withHour(10));
        appointment.setDurationMinutes(60);
        appointment.setEndTime(appointment.getStartTime().plusMinutes(60));
        appointment.setCompany(company);
        appointment.setEmployee(employee);

        when(employeeUnavailabilityRepository.findByEmployeeIdAndUnavailableFromBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        assertDoesNotThrow(() -> appointmentService.createAppointment(appointment));
    }

    @Test
    void testUpdateAppointmentWithinTwoWeeks() {
        Company company = new Company();
        company.setCompanyName("CompanyA");
        company.setWorkStartTime(LocalTime.of(9, 0));
        company.setWorkEndTime(LocalTime.of(17, 0));

        User employee = new User();
        employee.setId(1L);
        employee.setCompany(company);

        Appointment existingAppointment = new Appointment();
        existingAppointment.setStartTime(LocalDateTime.now().plusDays(5));
        existingAppointment.setCompany(company);
        existingAppointment.setEmployee(employee);

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setStartTime(LocalDateTime.now().plusDays(10).withHour(10));
        updatedAppointment.setEndTime(updatedAppointment.getStartTime().plusMinutes(60));
        updatedAppointment.setCompany(company);
        updatedAppointment.setEmployee(employee);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(existingAppointment));
        when(employeeUnavailabilityRepository.findByEmployeeIdAndUnavailableFromBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedAppointment);

        assertDoesNotThrow(() -> appointmentService.updateAppointment(1L, updatedAppointment));
    }
}
