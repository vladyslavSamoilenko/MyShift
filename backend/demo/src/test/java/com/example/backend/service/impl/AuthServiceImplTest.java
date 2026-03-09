package com.example.backend.service.impl;

import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entities.Project;
import com.example.backend.model.entities.RefreshToken;
import com.example.backend.model.entities.User;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.model.JwtTokenProvider;
import com.example.backend.security.model.profiles.UserProfileDTO;
import com.example.backend.security.model.requests.LoginRequest;
import com.example.backend.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_ValidCredentials_ReturnsUserProfileDTOWithTokens() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        Project mockProject = new Project();
        mockProject.setId(10);

        User mockUser = new User();
        mockUser.setEmail("test@test.com");
        mockUser.setProject(mockProject);
        mockUser.setRole(com.example.backend.model.enums.Role.ADMIN);

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setToken("refresh-token-123");
        UserProfileDTO mockProfile = new UserProfileDTO(
                1,
                "test@test.com",
                10,
                com.example.backend.model.enums.Role.ADMIN,
                "access-token-123",
                "refresh-token-123"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.of(mockUser));
        when(refreshTokenService.generateOrUpdateRefreshToken(mockUser)).thenReturn(mockRefreshToken);
        when(jwtTokenProvider.generateToken(mockUser)).thenReturn("access-token-123");
        when(userMapper.toUserProfileDTO(mockUser, "access-token-123", "refresh-token-123")).thenReturn(mockProfile);

        GeneralResponse<UserProfileDTO> response = authService.login(request);

        assertTrue(response.isSuccess());
        assertNotNull(response.getPayload());
        assertEquals("access-token-123", response.getPayload().getToken());
        assertEquals(10, response.getPayload().getProjectId());
        verify(authenticationManager, times(1)).authenticate(any());
    }
}
