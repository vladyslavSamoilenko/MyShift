package com.example.backend.service;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.request.post.userRequests.UserOwnerRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.security.model.profiles.UserProfileDTO;
import com.example.backend.security.model.requests.LoginRequest;

public interface AuthService {
    GeneralResponse<UserProfileDTO> login(LoginRequest loginRequest);
    GeneralResponse<UserDTO> registerUserOwner(UserOwnerRequest userOwnerRequest);
}
