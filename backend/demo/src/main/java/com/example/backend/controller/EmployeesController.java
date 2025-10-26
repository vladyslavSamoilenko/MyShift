package com.example.backend.controller;

import com.example.backend.model.constants.ApiLogMessage;
import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.request.post.employeeRequests.EmployeeRequest;
import com.example.backend.model.request.post.employeeRequests.UpdateEmployeeRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.service.EmployeeService;
import com.example.backend.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    private ResponseEntity<GeneralResponse<EmployeeDTO>> getEmployeeById(@PathVariable(name = "id") Integer employeeId){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<EmployeeDTO> response = employeeService.getById(employeeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    private ResponseEntity<GeneralResponse<EmployeeDTO>> createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());
        GeneralResponse<EmployeeDTO> response = employeeService.createEmployee(employeeRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    private ResponseEntity<GeneralResponse<EmployeeDTO>> updateEmployeeById(@PathVariable(name = "id") Integer employeeId, @RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<EmployeeDTO> updateEmpoyee = employeeService.updateEmployee(employeeId,updateEmployeeRequest);
        return ResponseEntity.ok(updateEmpoyee);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<GeneralResponse<EmployeeDTO>> softDeleteEmployeeById(@PathVariable(name = "id") Integer id){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        employeeService.softDeleteEmployee(id);
        return ResponseEntity.ok().build();
    }
}
