package com.phoenix_dev.rezervation_management.service;

import com.phoenix_dev.rezervation_management.model.Company;
import com.phoenix_dev.rezervation_management.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCompaniesReturnsEmptyList() {
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(companyService.getAllCompanies().isEmpty());
    }

    @Test
    void getAllCompaniesReturnsNonEmptyList() {
        Company company = new Company();
        when(companyRepository.findAll()).thenReturn(Collections.singletonList(company));
        assertFalse(companyService.getAllCompanies().isEmpty());
    }

    @Test
    void getCompanyByIdReturnsCompany() {
        Company company = new Company();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        assertEquals(company, companyService.getCompanyById(1L));
    }

    @Test
    void getCompanyByIdThrowsExceptionWhenNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> companyService.getCompanyById(1L));
    }

    @Test
    void createCompanySavesAndReturnsCompany() {
        Company company = new Company();
        when(companyRepository.save(company)).thenReturn(company);
        assertEquals(company, companyService.createCompany(company));
    }

    @Test
    void updateCompanyUpdatesAndReturnsCompany() {
        Company existingCompany = new Company();
        existingCompany.setId(1L);
        Company updatedCompany = new Company();
        updatedCompany.setCompanyName("UpdatedName");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(existingCompany)).thenReturn(existingCompany);

        assertEquals(existingCompany, companyService.updateCompany(1L, updatedCompany));
        assertEquals("UpdatedName", existingCompany.getCompanyName());
    }

    @Test
    void deleteCompanyDeletesCompany() {
        doNothing().when(companyRepository).deleteById(1L);
        companyService.deleteCompany(1L);
        verify(companyRepository, times(1)).deleteById(1L);
    }
}