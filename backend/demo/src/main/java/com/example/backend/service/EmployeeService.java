package com.example.backend.service;

import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.request.post.EmployeeRequest;
import com.example.backend.model.request.post.ProjectRequest;
import com.example.backend.model.request.post.UpdateEmployeeRequest;
import com.example.backend.model.request.post.UpdateProjectRequest;
import com.example.backend.model.response.GeneralResponse;
import jakarta.validation.constraints.NotNull;

public interface EmployeeService{
    GeneralResponse<EmployeeDTO> getById(@NotNull Integer id);
    GeneralResponse<EmployeeDTO> createEmployee(@NotNull EmployeeRequest employeeRequest);
    GeneralResponse<EmployeeDTO> updateEmployee(@NotNull Integer employeeId, @NotNull UpdateEmployeeRequest updateEmployeeRequest);
    void softDeleteEmployee(@NotNull Integer employeeId);
}
