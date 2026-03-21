package com.example.backend.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import com.example.backend.model.request.post.userRequests.RegisterUserOwnerRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.security.model.profiles.UserProfileDTO;
import com.example.backend.security.model.requests.LoginRequest;
import com.example.backend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {com.example.backend.security.model.JwtRequestFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_ValidRequest_Returns200AndSetsCookie() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        UserProfileDTO mockProfile = new UserProfileDTO(
                1,                                          // id
                "test@test.com",                            // email
                10,                                         // projectId
                99,                                         // employeeId (ВОТ ОНО, НАШЕ НОВОЕ ПОЛЕ!)
                com.example.backend.model.enums.Role.ADMIN, // role
                "access-token-123",                         // token
                "refresh-token-123"                         // refreshToken
        );

        GeneralResponse<UserProfileDTO> expectedResponse = GeneralResponse.createSuccessfulWithNewToken(mockProfile);

        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.token").value("access-token-123"));
    }

    @Test
    void registerUserOwner_ValidRequest_Returns200() throws Exception {
        RegisterUserOwnerRequest request = new RegisterUserOwnerRequest();

        UserProfileDTO mockProfile = new UserProfileDTO(
                1,                                          // id
                "test@test.com",                            // email
                10,                                         // projectId
                99,                                         // employeeId (ВОТ ОНО, НАШЕ НОВОЕ ПОЛЕ!)
                com.example.backend.model.enums.Role.ADMIN, // role
                "access-token-123",                         // token
                "refresh-token-123"                         // refreshToken
        );

        GeneralResponse<UserProfileDTO> expectedResponse = GeneralResponse.createSuccessfulWithNewToken(mockProfile);
        when(authService.registerUserOwner(any(RegisterUserOwnerRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/registerUserOwner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.token").value("access-token-123"));
    }

    @Test
    void refreshToken_ValidToken_Returns200() throws Exception {
        String oldRefreshToken = "old-refresh-123";

        UserProfileDTO mockProfile = new UserProfileDTO(
                1,                                          // id
                "test@test.com",                            // email
                10,                                         // projectId
                99,                                         // employeeId (ВОТ ОНО, НАШЕ НОВОЕ ПОЛЕ!)
                com.example.backend.model.enums.Role.ADMIN, // role
                "access-token-123",                         // token
                "refresh-token-123"                         // refreshToken
        );

        GeneralResponse<UserProfileDTO> expectedResponse = GeneralResponse.createSuccessfulWithNewToken(mockProfile);

        when(authService.refreshAccessToken(anyString())).thenReturn(expectedResponse);

        mockMvc.perform(get("/auth/refresh/token")
                        .param("token", oldRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.token").value("access-token-123"));
    }
}
