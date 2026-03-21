package com.example.backend.service.impl;

import com.example.backend.model.dto.LeaveRequestDTO;
import com.example.backend.model.entities.LeaveRequest;
import com.example.backend.model.entities.User;
import com.example.backend.model.enums.LeaveStatus;
import com.example.backend.repository.LeaveRequestRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    public LeaveRequest createLeaveRequest(LeaveRequestDTO dto) {
        LeaveRequest request = new LeaveRequest();

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            request.setEmployeeId(Long.valueOf(user.getEmployee().getId()));
        } else {
            request.setEmployeeId(dto.getEmployeeId());
        }

        request.setProjectId(dto.getProjectId());
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setType(dto.getType());
        request.setReason(dto.getReason());

        return leaveRequestRepository.save(request);
    }
    public List<LeaveRequest> getEmployeeLeaves(Long employeeId) {
        return leaveRequestRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    public List<LeaveRequest> getProjectLeaves(Long projectId) {
        return leaveRequestRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }
    public LeaveRequest updateStatus(Long id, LeaveStatus newStatus) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono wniosku o ID: " + id));

        request.setStatus(newStatus);
        return leaveRequestRepository.save(request);
    }
}
