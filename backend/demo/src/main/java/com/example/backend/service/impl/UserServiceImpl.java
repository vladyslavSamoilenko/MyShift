package com.example.backend.service.impl;

import com.example.backend.mapper.UserMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.User;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.UserUpdateRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

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
    public GeneralResponse<UserDTO> createUser(User user) {
        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new DataExistException(ApiErrorMessage.USER_ALREADY_EXISTS_BY_EMAIL.getMessage(user.getEmail()));
        }

        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.toUserDTO(savedUser);
        return GeneralResponse.createSuccessful(userDTO);
    }

    @Override
    public GeneralResponse<UserDTO> updateUser(Integer userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(
                ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        userMapper.updateUser(user, userUpdateRequest);
        user = userRepository.save(user);

        UserDTO userDTO = userMapper.toUserDTO(user);
        return GeneralResponse.createSuccessful(userDTO);
    }

    @Override
    public void softDelete(Integer userID) {
        User user = userRepository.findUserById(userID).orElseThrow(() -> new NotFoundException(ApiErrorMessage.EMPLOYEE_NOT_FOUND_BY_ID.getMessage(userID)));
        user.setDeleted(true);
        userRepository.save(user);
    }

    public String createDefaultPassword() {
        char [] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%&".toCharArray();
        Random random = new Random();

        StringBuilder password = new StringBuilder();
        for(int i = 0; i < 12; i++){
            int randomIndex = random.nextInt(68);
            password.append(chars[randomIndex]);
        }
        return password.toString();

    }
}
