package com.example.backend.controller;

import com.example.backend.model.dto.LeaveRequestDTO;
import com.example.backend.model.entities.LeaveRequest;
import com.example.backend.model.enums.LeaveStatus;
import com.example.backend.service.impl.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaves")
@CrossOrigin
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequestDTO dto) {
        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(dto));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequest>> getEmployeeLeaves(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveRequestService.getEmployeeLeaves(employeeId));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyAuthority('WORKER', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<LeaveRequest>> getProjectLeaves(@PathVariable Long projectId) {
        return ResponseEntity.ok(leaveRequestService.getProjectLeaves(projectId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<LeaveRequest> updateLeaveStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {

        LeaveStatus newStatus = LeaveStatus.valueOf(statusUpdate.get("status").toUpperCase());
        return ResponseEntity.ok(leaveRequestService.updateStatus(id, newStatus));
    }
}
