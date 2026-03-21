package com.example.backend.service;

import com.example.backend.model.dto.AvailabilityDTO;
import com.example.backend.model.entities.Availability;

import java.util.List;

public interface AvailabilityService {
    List<Availability> getEmployeeAvailabilities(Long employeeId);
    Availability saveOrUpdate(AvailabilityDTO dto);
}
