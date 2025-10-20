package com.example.backend.model.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Must contains employee email")
    private String email;
    @NotBlank(message = "Must contains employee password")
    private String password;
    @NotBlank(message = "Must contains role")
    private String role;
    @NotNull(message = "Must contains employee id")
    private Integer employeeId;
}
