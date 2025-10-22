package com.example.backend.service;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.User;
import com.example.backend.model.request.post.userRequests.UserUpdateRequest;
import com.example.backend.model.response.GeneralResponse;
import jakarta.validation.constraints.NotNull;

public interface UserService{
    GeneralResponse<UserDTO> getById(@NotNull Integer id);
    GeneralResponse<UserDTO> createUser(@NotNull User user);
    GeneralResponse<UserDTO> updateUser(@NotNull Integer userId,
                                        @NotNull UserUpdateRequest userUpdateRequest);
    void softDelete(@NotNull Integer userID);
    String createDefaultPassword();
}
