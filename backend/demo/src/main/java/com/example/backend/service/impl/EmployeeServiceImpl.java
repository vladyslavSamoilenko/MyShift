package com.example.backend.service.impl;

import com.example.backend.mapper.EmployeeMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.entities.User;
import com.example.backend.model.enums.Role;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.employeeRequests.EmployeeRequest;
import com.example.backend.model.request.post.employeeRequests.UpdateEmployeeRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.service.EmployeeService;
import com.example.backend.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserService userService;

    @Override
    public GeneralResponse<EmployeeDTO> getById(@NotNull Integer id) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.EMPLOYEE_NOT_FOUND_BY_ID.getMessage(id)));
        EmployeeDTO employeeDTO = employeeMapper.toEmployeeDTO(employee);
        return GeneralResponse.createSuccessful(employeeDTO);
    }

    @Override
    public GeneralResponse<EmployeeDTO> createEmployee(@NotNull EmployeeRequest employeeRequest) {
        if(employeeRepository.existsByEmail(employeeRequest.getEmail())){
            throw new DataExistException(ApiErrorMessage.EMPLOYEE_ALREADY_EXIST_BY_EMAIL.getMessage(employeeRequest.getEmail()));
        }
        else if (employeeRepository.existsByPhone(employeeRequest.getPhone())) {
            throw new DataExistException(ApiErrorMessage.EMPLOYEE_ALREADY_EXIST_BY_PHONE.getMessage(employeeRequest.getPhone()));
        }

        Employee employee = employeeMapper.createEmployee(employeeRequest);
        Employee savedEmployee = employeeRepository.save(employee);

        User user = new User();
        user.setEmail(employee.getEmail());
        user.setPassword(userService.createDefaultPassword());
        user.setRole(Role.valueOf(employeeRequest.getRole()));
        user.setEmployee(employee);
        userService.createUser(user);

        EmployeeDTO employeeDTO = employeeMapper.toEmployeeDTO(savedEmployee);
        return GeneralResponse.createSuccessful(employeeDTO);
    }

    @Override
    public GeneralResponse<EmployeeDTO> updateEmployee(@NotNull Integer employeeId,@NotNull UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.EMPLOYEE_NOT_FOUND_BY_ID.getMessage(employeeId)));
        employeeMapper.updateEmployee(employee, updateEmployeeRequest);
        employee.setUpdatedAt(LocalDateTime.now());
        employee = employeeRepository.save(employee);

        EmployeeDTO employeeDTO = employeeMapper.toEmployeeDTO(employee);
        return GeneralResponse.createSuccessful(employeeDTO);
    }

    @Override
    public void softDeleteEmployee(@NotNull Integer employeeId) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.EMPLOYEE_NOT_FOUND_BY_ID.getMessage(employeeId)));
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }
}
