package com.example.backend.service;

import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.request.post.employeeRequests.EmployeeRequest;
import com.example.backend.model.request.post.employeeRequests.UpdateEmployeeRequest;
import com.example.backend.model.response.GeneralResponse;
import jakarta.validation.constraints.NotNull;

public interface EmployeeService{
    GeneralResponse<EmployeeDTO> getById(@NotNull Integer id);
    GeneralResponse<EmployeeDTO> createEmployee(@NotNull EmployeeRequest employeeRequest);
    GeneralResponse<EmployeeDTO> updateEmployee(@NotNull Integer employeeId, @NotNull UpdateEmployeeRequest updateEmployeeRequest);
    void softDeleteEmployee(@NotNull Integer employeeId);
}
