package com.phoenix_dev.rezervation_management.service;

import com.phoenix_dev.rezervation_management.exception.CompanyNotFoundException;
import com.phoenix_dev.rezervation_management.model.Company;
import com.phoenix_dev.rezervation_management.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cankat Sezer
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        Company company = getCompanyById(id);
        company.setCompanyName(updatedCompany.getCompanyName());
        company.setCompanyAddress(updatedCompany.getCompanyAddress());
        company.setCompanyPhone(updatedCompany.getCompanyPhone());
        company.setCompanyEmail(updatedCompany.getCompanyEmail());
        company.setCompanyWebSite(updatedCompany.getCompanyWebSite());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new CompanyNotFoundException("Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }
}
