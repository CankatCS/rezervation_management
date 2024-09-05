package com.phoenix_dev.rezervation_management.util;

import com.phoenix_dev.rezervation_management.model.*;
import com.phoenix_dev.rezervation_management.repository.CompanyRepository;
import com.phoenix_dev.rezervation_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Company company1 = new Company();
        company1.setCompanyName("TechCorp");
        company1.setCompanyAddress("123 Tech Street");
        company1.setCompanyPhone("123-456-7890");
        company1.setCompanyEmail("contact@techcorp.com");
        company1.setCompanyWebSite("www.techcorp.com");
        company1.setWorkStartTime(LocalTime.of(9, 0));
        company1.setWorkEndTime(LocalTime.of(17, 0));

        Company company2 = new Company();
        company2.setCompanyName("BizInc");
        company2.setCompanyAddress("456 Biz Avenue");
        company2.setCompanyPhone("987-654-3210");
        company2.setCompanyEmail("info@bizinc.com");
        company2.setCompanyWebSite("www.bizinc.com");
        company2.setWorkStartTime(LocalTime.of(8, 0));
        company2.setWorkEndTime(LocalTime.of(16, 0));

        companyRepository.save(company1);
        companyRepository.save(company2);

        // Create 5 customers
        for (int i = 1; i <= 5; i++) {
            User customer = new User();
            customer.setUsername("customer" + i);
            customer.setPassword(passwordEncoder.encode("password" + i));
            customer.setEmail("customer" + i + "@example.com");
            Set<Role> customerRoles = new HashSet<>();
            customerRoles.add(Role.CUSTOMER);
            customer.setRoles(customerRoles);
            userRepository.save(customer);
        }

        // Create 5 employees for each company
        for (int i = 1; i <= 5; i++) {
            User employee = new User();
            employee.setUsername("employee" + i + "_techcorp");
            employee.setPassword(passwordEncoder.encode("password" + i));
            employee.setEmail("employee" + i + "@techcorp.com");
            Set<Role> employeeRoles = new HashSet<>();
            employeeRoles.add(Role.EMPLOYEE);
            employee.setRoles(employeeRoles);
            employee.setCompany(company1);
            userRepository.save(employee);
        }

        for (int i = 6; i <= 10; i++) {
            User employee = new User();
            employee.setUsername("employee" + (i - 5) + "_bizinc");
            employee.setPassword(passwordEncoder.encode("password" + i));
            employee.setEmail("employee" + (i - 5) + "@bizinc.com");
            Set<Role> employeeRoles = new HashSet<>();
            employeeRoles.add(Role.EMPLOYEE);
            employee.setRoles(employeeRoles);
            employee.setCompany(company2);
            userRepository.save(employee);
        }

        // Create 2 company admins for each company
        for (int i = 1; i <= 2; i++) {
            User companyAdmin = new User();
            companyAdmin.setUsername("admin" + i + "_techcorp");
            companyAdmin.setPassword(passwordEncoder.encode("admin_pw" + i));
            companyAdmin.setEmail("admin" + i + "@techcorp.com");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(Role.COMPANY_ADMIN);
            companyAdmin.setRoles(adminRoles);
            companyAdmin.setCompany(company1);
            userRepository.save(companyAdmin);
        }

        for (int i = 1; i <= 2; i++) {
            User companyAdmin = new User();
            companyAdmin.setUsername("admin" + i + "_bizinc");
            companyAdmin.setPassword(passwordEncoder.encode("password" + (i + 2)));
            companyAdmin.setEmail("admin" + i + "@bizinc.com");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(Role.COMPANY_ADMIN);
            companyAdmin.setRoles(adminRoles);
            companyAdmin.setCompany(company2);
            userRepository.save(companyAdmin);
        }

        // Create 1 system admin
        User systemAdmin = new User();
        systemAdmin.setUsername("cankat");
        systemAdmin.setPassword(passwordEncoder.encode("sezer"));
        systemAdmin.setEmail("cankat.sezer@example.com");
        Set<Role> systemAdminRoles = new HashSet<>();
        systemAdminRoles.add(Role.SYSTEM_ADMIN);
        systemAdmin.setRoles(systemAdminRoles);
        userRepository.save(systemAdmin);
    }
}
