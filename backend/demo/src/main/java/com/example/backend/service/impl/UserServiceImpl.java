package com.example.backend.service.impl;

import com.example.backend.mapper.UserMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.User;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.UserRequest;
import com.example.backend.model.request.post.UserUpdateRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public GeneralResponse<UserDTO> getById(@NotNull Integer id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(id)));
        UserDTO userDTO = userMapper.toUserDTO(user);
        return GeneralResponse.createSuccessful(userDTO);
    }

    @Override
    public GeneralResponse<UserDTO> createUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public GeneralResponse<UserDTO> updateUser(Integer userId, UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @Override
    public void softDelete(Integer userID) {

    }
}
