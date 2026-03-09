package com.example.backend.controller;

import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ProjectsController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {com.example.backend.security.model.JwtRequestFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void getProjectById_ShouldReturn200AndProjectData() throws Exception {
        Integer projectId = 1;
        ProjectDTO projectDTO = new ProjectDTO();

        GeneralResponse<ProjectDTO> expectedResponse = GeneralResponse.createSuccessful(projectDTO);
        when(projectService.getById(projectId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
