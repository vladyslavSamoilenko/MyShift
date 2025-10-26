package com.example.backend.model.request.post.userRequests;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequest {
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
