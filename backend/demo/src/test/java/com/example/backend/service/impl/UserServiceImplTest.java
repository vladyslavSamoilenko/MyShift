package com.example.backend.service.impl;

import com.example.backend.mapper.EmployeeMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.User;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getById_Success_ReturnsUserDTO() {
        // Arrange
        Integer userId = 1;
        User mockUser = new User();
        mockUser.setId(userId);

        UserDTO mockUserDTO = new UserDTO(); // Предполагаем, что у вас есть пустой конструктор

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserDTO(mockUser)).thenReturn(mockUserDTO);

        // Act
        GeneralResponse<UserDTO> response = userService.getById(userId);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals(mockUserDTO, response.getPayload());
        verify(userRepository, times(1)).findUserById(userId);
    }

    @Test
    void getById_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        Integer userId = 99;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.getById(userId));
        verify(userRepository, times(1)).findUserById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void createUser_Success_ReturnsUserDTO() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("test@test.com");

        User savedUser = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toUserDTO(savedUser)).thenReturn(userDTO);

        // Act
        GeneralResponse<UserDTO> response = userService.createUser(newUser);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals(userDTO, response.getPayload());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsDataExistException() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("existing@test.com");

        when(userRepository.existsUserByEmail(newUser.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DataExistException.class, () -> userService.createUser(newUser));
        verify(userRepository, never()).save(any());
    }
}
