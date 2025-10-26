package com.example.backend.model.request.post.shiftRequests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDateRequest {
    @NotBlank(message = "Must contains shift date")
    private String localDate;
}
