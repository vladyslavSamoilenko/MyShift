package com.example.backend.service.impl;

import com.example.backend.mapper.ProjectMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.projectRequests.ProjectRequest;
import com.example.backend.model.request.post.projectRequests.UpdateProjectRequest;
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
    private final ProjectMapper projectMapper;

    @Override
    public GeneralResponse<ProjectDTO> getById(@NotNull Integer id) {
        Project project = projectRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.PROJECT_NOT_FOUND_BY_ID.getMessage(id)));

        ProjectDTO projectDTO = projectMapper.toProjectDTO(project);

        return GeneralResponse.createSuccessful(projectDTO);
    }

    @Override
    public GeneralResponse<ProjectDTO> createProject(@NotNull ProjectRequest projectRequest) {
        if(projectRepository.existsByName(projectRequest.getName())){
            throw new DataExistException(ApiErrorMessage.PROJECT_ALREADY_EXIST.getMessage(projectRequest.getName()));
        }

        Project project = projectMapper.createProject(projectRequest);
        Project savedProject = projectRepository.save(project);
        ProjectDTO projectDTO = projectMapper.toProjectDTO(savedProject);

        return GeneralResponse.createSuccessful(projectDTO);
    }

    @Override
    public GeneralResponse<ProjectDTO> updateProject(@NotNull Integer projectId,@NotNull UpdateProjectRequest request) {
        Project project = projectRepository.findByIdAndDeletedFalse(projectId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.PROJECT_NOT_FOUND_BY_ID.getMessage(projectId)));
        projectMapper.updateProject(project, request);
        project = projectRepository.save(project);

        ProjectDTO projectDTO = projectMapper.toProjectDTO(project);
        return GeneralResponse.createSuccessful(projectDTO);
    }

    @Override
    public void softDeleteProject(@NotNull Integer projectId) {
        Project project = projectRepository.findByIdAndDeletedFalse(projectId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.PROJECT_NOT_FOUND_BY_ID.getMessage(projectId)));
        project.setDeleted(true);
        projectRepository.save(project);
    }


}
