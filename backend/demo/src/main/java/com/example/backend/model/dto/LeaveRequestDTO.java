package com.example.backend.model.dto;

import com.example.backend.model.enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO implements Serializable {
    private Integer userId;
    private Long employeeId;
    private Long projectId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType type;
    private String reason;
}
