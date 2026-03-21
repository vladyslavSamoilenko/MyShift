package com.example.backend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class AvailabilityDTO implements Serializable {
    private Long employeeId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
