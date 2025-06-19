package com.example.backend.service;

import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.response.GeneralResponse;
import jakarta.validation.constraints.NotNull;

public interface ProjectService {
    GeneralResponse<ProjectDTO> getById(@NotNull Integer id);
}
