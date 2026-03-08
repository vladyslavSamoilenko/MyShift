package com.example.backend.service;

import com.example.backend.model.request.post.userRequests.RegisterUserOwnerRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.security.model.profiles.UserProfileDTO;
import com.example.backend.security.model.requests.LoginRequest;

public interface AuthService {
    GeneralResponse<UserProfileDTO> login(LoginRequest loginRequest);
    GeneralResponse<UserProfileDTO> registerUserOwner(RegisterUserOwnerRequest registerUserOwnerRequest);

    GeneralResponse<UserProfileDTO> refreshAccessToken(String refreshToken);
}
