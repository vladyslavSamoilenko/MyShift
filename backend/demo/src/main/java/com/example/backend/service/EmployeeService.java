package com.example.backend.service;

import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.request.post.employeeRequests.EmployeeRequest;
import com.example.backend.model.request.post.employeeRequests.UpdateEmployeeRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface EmployeeService{
    GeneralResponse<EmployeeDTO> getById(@NotNull Integer id);
    GeneralResponse<EmployeeDTO> createEmployee(@NotNull EmployeeRequest employeeRequest);
    GeneralResponse<EmployeeDTO> updateEmployee(@NotNull Integer employeeId, @NotNull UpdateEmployeeRequest updateEmployeeRequest);
    GeneralResponse<PaginationResponse<EmployeeDTO>> getAllByProjectId(Integer projectId, int page, int limit);
    void softDeleteEmployee(@NotNull Integer employeeId);
}
