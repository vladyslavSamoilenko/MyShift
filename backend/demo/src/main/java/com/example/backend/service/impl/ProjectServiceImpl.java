package com.example.backend.service.impl;

import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.service.ProjectService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public GeneralResponse<ProjectDTO> getById(@NotNull Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.PROJECT_NOT_FOUND_BY_ID.getMessage(id)));

        ProjectDTO projectDTO = ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .shifts(project.getShifts())
                .build();

        return GeneralResponse.createSuccessful(projectDTO);
    }


}
