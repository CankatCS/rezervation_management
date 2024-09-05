package com.phoenix_dev.rezervation_management.repository;

/**
 * @author Cankat Sezer
 */
import com.phoenix_dev.rezervation_management.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByCompanyName(String companyName);
}