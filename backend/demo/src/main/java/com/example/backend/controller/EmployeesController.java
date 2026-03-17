package com.example.backend.controller;

import com.example.backend.model.constants.ApiLogMessage;
import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.request.post.employeeRequests.EmployeeRequest;
import com.example.backend.model.request.post.employeeRequests.UpdateEmployeeRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import com.example.backend.service.EmployeeService;
import com.example.backend.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<EmployeeDTO>> getEmployeeById(@PathVariable(name = "id") Integer employeeId){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<EmployeeDTO> response = employeeService.getById(employeeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<GeneralResponse<EmployeeDTO>> createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());
        GeneralResponse<EmployeeDTO> response = employeeService.createEmployee(employeeRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<GeneralResponse<EmployeeDTO>> updateEmployeeById(@PathVariable(name = "id") Integer employeeId, @RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<EmployeeDTO> updateEmpoyee = employeeService.updateEmployee(employeeId,updateEmployeeRequest);
        return ResponseEntity.ok(updateEmpoyee);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<GeneralResponse<EmployeeDTO>> softDeleteEmployeeById(@PathVariable(name = "id") Integer id){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        employeeService.softDeleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<GeneralResponse<PaginationResponse<EmployeeDTO>>> getEmployeesByProjectId(
            @PathVariable(name = "projectId") Integer projectId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "100") int limit) {

        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<PaginationResponse<EmployeeDTO>> response = employeeService.getAllByProjectId(projectId, page, limit);
        return ResponseEntity.ok(response);
    }

}
