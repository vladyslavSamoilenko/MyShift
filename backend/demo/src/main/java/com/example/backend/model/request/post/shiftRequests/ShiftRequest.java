package com.example.backend.model.request.post.shiftRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRequest {
    @NotBlank(message = "Must contains shift`s date dd-MM-yyyy")
    private String shiftDate;
    @NotBlank(message = "Must contains shift`s start time hh:mm")
    private String startTime;
    @NotBlank(message = "Must contains shift`s end time hh:mm")
    private String endTime;
    @NotBlank(message = "Must contains project id")
    private Integer projectId;
    @NotBlank(message = "Must contains user id")
    private Integer userId;
    @NotEmpty(message = "Must contain at least one employee")
    private List<Integer> employeesIds;
}
