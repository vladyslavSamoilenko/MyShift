package com.example.backend.service;

import com.example.backend.model.response.GeneralResponse;
import com.example.backend.security.model.profiles.UserProfileDTO;
import com.example.backend.security.model.requests.LoginRequest;

public interface AuthService {
    GeneralResponse<UserProfileDTO> login(LoginRequest loginRequest);
}
