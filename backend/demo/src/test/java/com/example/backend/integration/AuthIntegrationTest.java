package com.example.backend.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.model.enums.Role;
import com.example.backend.model.request.post.userRequests.RegisterUserOwnerRequest;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void registerUserOwner_FullFlow_SavesToDatabaseAndReturnsToken() throws Exception {
        // 1. Arrange: Подготавливаем реальные данные для запроса
        RegisterUserOwnerRequest.UserDto userData = new RegisterUserOwnerRequest.UserDto();
        userData.setEmail("integration@test.com");
        userData.setPassword("StrongPass123!");
        userData.setConfirmPassword("StrongPass123!");

        RegisterUserOwnerRequest.EmployeeDto employeeData = new RegisterUserOwnerRequest.EmployeeDto();
        employeeData.setFirstName("Ivan");
        employeeData.setLastName("Ivanov");
        employeeData.setPhone("+1234567890");
        employeeData.setRole(Role.ADMIN.name());

        RegisterUserOwnerRequest.ProjectDTO projectData = new RegisterUserOwnerRequest.ProjectDTO();
        projectData.setName("Mega Integration Project");
        projectData.setDescription("Testing the full flow");

        RegisterUserOwnerRequest request = new RegisterUserOwnerRequest();
        request.setUserData(userData);
        request.setEmployeeData(employeeData);
        request.setProjectData(projectData);

        // 2. Act: Отправляем реальный HTTP-запрос в наш контроллер
        mockMvc.perform(post("/auth/registerUserOwner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                // 3. Assert (HTTP): Проверяем, что ответ успешный и токен сгенерирован
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.token").exists())
                .andExpect(jsonPath("$.payload.email").value("integration@test.com"));

        // 4. Assert (Database): Идем в базу данных и проверяем, что всё реально сохранилось!
        assertTrue(userRepository.existsUserByEmail("integration@test.com"), "User should be saved in DB");
        assertTrue(projectRepository.existsByName("Mega Integration Project"), "Project should be saved in DB");
        assertTrue(employeeRepository.existsByPhone("+1234567890"), "Employee should be saved in DB");
    }
}
