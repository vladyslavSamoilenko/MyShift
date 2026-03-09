package com.example.backend.service.impl;

import com.example.backend.mapper.ProjectMapper;
import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.request.post.userRequests.RegisterUserOwnerRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void getById_Success_ReturnsProjectDTO() {
        Integer projectId = 1;
        Project mockProject = new Project();
        mockProject.setId(projectId);

        ProjectDTO expectedDto = new ProjectDTO();

        when(projectRepository.findByIdAndDeletedFalse(projectId)).thenReturn(Optional.of(mockProject));
        when(projectMapper.toProjectDTO(mockProject)).thenReturn(expectedDto);

        GeneralResponse<ProjectDTO> response = projectService.getById(projectId);

        assertTrue(response.isSuccess());
        assertEquals(expectedDto, response.getPayload());
        verify(projectRepository, times(1)).findByIdAndDeletedFalse(projectId);
    }

    @Test
    void createProject_NameAlreadyExists_ThrowsDataExistException() {
        RegisterUserOwnerRequest.ProjectDTO projectData = new RegisterUserOwnerRequest.ProjectDTO();
        projectData.setName("Existing Project");

        RegisterUserOwnerRequest request = new RegisterUserOwnerRequest();
        request.setProjectData(projectData);

        when(projectRepository.existsByName("Existing Project")).thenReturn(true);

        assertThrows(DataExistException.class, () -> projectService.createProject(request));
        verify(projectRepository, never()).save(any());
    }
}
