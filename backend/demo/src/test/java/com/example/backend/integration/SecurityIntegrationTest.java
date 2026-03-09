package com.example.backend.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.model.entities.Project;
import com.example.backend.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Поднимаем всё приложение (включая Security)
@AutoConfigureMockMvc
@ActiveProfiles("test") // Используем H2 базу данных
@Transactional
public class SecurityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    private Integer testProjectId;

    @BeforeEach
    void setUp() {
        Project project = new Project();
        project.setName("Test Security Project");
        project.setDescription("Description");
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        project.setDeleted(false);

        Project savedProject = projectRepository.save(project);
        testProjectId = savedProject.getId();
    }

    @Test
    void deleteProject_WithoutAuth_Returns401Unauthorized() throws Exception {
        mockMvc.perform(delete("/projects/{id}", testProjectId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"WORKER"})
    void deleteProject_WithWorkerRole_Returns403Forbidden() throws Exception {
        mockMvc.perform(delete("/projects/{id}", testProjectId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deleteProject_WithAdminRole_Returns200Ok() throws Exception {
        mockMvc.perform(delete("/projects/{id}", testProjectId))
                .andExpect(status().isOk());
    }
}
