package com.phoenix_dev.rezervation_management.controller;

/**
 * @author Cankat Sezer
 */

import com.phoenix_dev.rezervation_management.model.EmployeeUnavailability;
import com.phoenix_dev.rezervation_management.service.EmployeeUnavailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/unavailability")
public class EmployeeUnavailabilityController {

    private final EmployeeUnavailabilityService unavailabilityService;

    public EmployeeUnavailabilityController(EmployeeUnavailabilityService unavailabilityService) {
        this.unavailabilityService = unavailabilityService;
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<EmployeeUnavailability>> getUnavailabilityForEmployee(@PathVariable Long employeeId) {
        List<EmployeeUnavailability> unavailability = unavailabilityService.getUnavailabilityForNextTwoWeeks(employeeId);
        return ResponseEntity.ok(unavailability);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> addUnavailability(@RequestParam Long employeeId,
                                               @RequestParam LocalDateTime unavailableFrom,
                                               @RequestParam LocalDateTime unavailableUntil) {
        unavailabilityService.addUnavailability(employeeId, unavailableFrom, unavailableUntil);
        return ResponseEntity.ok("Unavailability added.");
    }

    @DeleteMapping("/remove/{unavailabilityId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> removeUnavailability(@PathVariable Long unavailabilityId) {
        unavailabilityService.removeUnavailability(unavailabilityId);
        return ResponseEntity.ok("Unavailability removed.");
    }
}
