package com.example.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO implements Serializable {
    private Integer id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate shiftDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Integer projectId;
    private Integer userId;
    private Integer employeeId;
    private String status;
    private Integer duration;
    private String durationFormatted;
}
