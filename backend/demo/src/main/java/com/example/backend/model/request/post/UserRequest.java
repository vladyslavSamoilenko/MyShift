package com.example.backend.model.request.post;

import com.example.backend.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequest {
    @NotBlank(message = "Must contains employee id")
    private Integer id;
    @NotBlank(message = "Must contains employee email")
    private String email;
    @NotBlank(message = "Must contains employee password")
    private String password;
    @NotBlank(message = "Must contains role")
    private String role;
    @NotBlank(message = "Must contains user id")
    private Integer userId;
}
