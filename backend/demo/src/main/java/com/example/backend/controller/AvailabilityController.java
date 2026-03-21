package com.example.backend.controller;

import com.example.backend.model.dto.AvailabilityDTO;
import com.example.backend.model.entities.Availability;
import com.example.backend.service.AvailabilityService;
import com.example.backend.service.impl.AvailabilityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availabilities")
@CrossOrigin
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityServiceImpl availabilityService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Availability>> getEmployeeAvailabilities(@PathVariable Long employeeId) {
        return ResponseEntity.ok(availabilityService.getEmployeeAvailabilities(employeeId));
    }

    @PostMapping("/save")
    public ResponseEntity<Availability> saveAvailability(@RequestBody AvailabilityDTO dto) {
        return ResponseEntity.ok(availabilityService.saveOrUpdate(dto));
    }
}
