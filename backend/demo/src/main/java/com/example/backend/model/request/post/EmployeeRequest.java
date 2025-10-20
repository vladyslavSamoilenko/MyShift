package com.example.backend.model.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest implements Serializable {
    @NotBlank(message = "Must contains first name")
    private String firstName;
    @NotBlank(message = "Must contains last name")
    private String lastName;
    @NotBlank(message = "Must contains phone number")
    private String phone;
    @NotBlank(message = "Must contains email")
    private String email;
    @NotBlank(message = "Must contains employee role: ADMIN, MANAGER, WORKER")
    private String role;
}
