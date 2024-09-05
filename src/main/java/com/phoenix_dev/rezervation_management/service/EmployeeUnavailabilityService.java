package com.phoenix_dev.rezervation_management.service;

/**
 * @author Cankat Sezer
 */

import com.phoenix_dev.rezervation_management.model.EmployeeUnavailability;
import com.phoenix_dev.rezervation_management.repository.EmployeeUnavailabilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeUnavailabilityService {

    private final EmployeeUnavailabilityRepository unavailabilityRepository;
    private final UserService userService;

    public EmployeeUnavailabilityService(EmployeeUnavailabilityRepository unavailabilityRepository, UserService userService) {
        this.unavailabilityRepository = unavailabilityRepository;
        this.userService = userService;
    }

    public List<EmployeeUnavailability> getUnavailabilityForEmployee(Long employeeId) {
        return unavailabilityRepository.findByEmployeeId(employeeId);
    }

    public void addUnavailability(Long employeeId, LocalDateTime unavailableFrom, LocalDateTime unavailableUntil) {
        EmployeeUnavailability unavailability = new EmployeeUnavailability();
        unavailability.setEmployee(userService.getUserById(employeeId));
        unavailability.setUnavailableFrom(unavailableFrom);
        unavailability.setUnavailableUntil(unavailableUntil);
        unavailabilityRepository.save(unavailability);
    }

    public void removeUnavailability(Long unavailabilityId) {
        unavailabilityRepository.deleteById(unavailabilityId);
    }

    public List<EmployeeUnavailability> getUnavailabilityForNextTwoWeeks(Long employeeId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoWeeksLater = now.plusWeeks(2);
        return unavailabilityRepository.findByEmployeeIdAndUnavailableFromBetween(employeeId, now, twoWeeksLater);
    }
}
