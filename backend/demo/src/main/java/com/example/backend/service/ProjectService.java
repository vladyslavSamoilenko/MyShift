package com.example.backend.service;

import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.request.post.projectRequests.UpdateProjectRequest;
import com.example.backend.model.request.post.userRequests.RegisterUserOwnerRequest;
import com.example.backend.model.response.GeneralResponse;
import jakarta.validation.constraints.NotNull;

public interface ProjectService {
    GeneralResponse<ProjectDTO> getById(@NotNull Integer id);

    Project createProject(@NotNull RegisterUserOwnerRequest registerUserOwnerRequest);

    GeneralResponse<ProjectDTO> updateProject(@NotNull Integer projectId, @NotNull UpdateProjectRequest updateProjectRequest);

    void softDeleteProject(@NotNull Integer projectId);
}
