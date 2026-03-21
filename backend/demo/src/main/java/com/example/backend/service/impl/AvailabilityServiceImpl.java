package com.example.backend.service.impl;

import com.example.backend.model.dto.AvailabilityDTO;
import com.example.backend.model.entities.Availability;
import com.example.backend.repository.AvailabilityRepository;
import com.example.backend.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {
    private final AvailabilityRepository availabilityRepository;

    public List<Availability> getEmployeeAvailabilities(Long employeeId) {
        return availabilityRepository.findByEmployeeId(employeeId);
    }

    public Availability saveOrUpdate(AvailabilityDTO dto) {
        Availability availability = availabilityRepository
                .findByEmployeeIdAndDayOfWeek(dto.getEmployeeId(), dto.getDayOfWeek())
                .orElse(new Availability());

        availability.setEmployeeId(dto.getEmployeeId());
        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());

        return availabilityRepository.save(availability);
    }
}
