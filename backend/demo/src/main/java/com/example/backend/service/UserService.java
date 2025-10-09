package com.example.backend.service;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.request.post.UserRequest;
import com.example.backend.model.request.post.UserUpdateRequest;
import com.example.backend.model.response.GeneralResponse;
import jakarta.validation.constraints.NotNull;

public interface UserService{
    GeneralResponse<UserDTO> getById(@NotNull Integer id);
    GeneralResponse<UserDTO> createUser(@NotNull UserRequest userRequest);
    GeneralResponse<UserDTO> updateUser(@NotNull Integer userId, @NotNull UserUpdateRequest userUpdateRequest);
    void softDelete(@NotNull Integer userID);
}
