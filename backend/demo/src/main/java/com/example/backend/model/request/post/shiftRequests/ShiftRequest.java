package com.example.backend.model.request.post.shiftRequests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRequest {
    @NotBlank(message = "Must contains shift`s date YYYY-MM-DD")
    private String shiftDate;
    @NotBlank(message = "Must contains shift`s start time hh:mm")
    private String startTime;
    @NotBlank(message = "Must contains shift`s end time hh:mm")
    private String endTime;
    @NotBlank(message = "Must contains project id")
    private Integer projectId;
    @NotBlank(message = "Must contains user id")
    private Integer userId;
}
