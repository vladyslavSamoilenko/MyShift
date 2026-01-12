package com.example.backend.service.impl;

import com.example.backend.mapper.ShiftMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.*;
import com.example.backend.model.enums.Status;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.shiftRequests.ShiftDateRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftSearchRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import com.example.backend.repository.*;
import com.example.backend.repository.cryteriaAPI.ShiftSearchCriteria;
import com.example.backend.service.ShiftService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftEmployeeRepository shiftEmployeeRepository;

    @Override
    @Transactional
    public GeneralResponse<ShiftDTO> getById(@NotNull Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id)));
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);
//        shiftDTO.setProjectId(shift.getProject().getId());
//        shiftDTO.setUserId(shift.getUser().getId());
        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
    @Transactional
    public GeneralResponse<ShiftDTO> createShift(ShiftRequest shiftRequest) {
        if(shiftRepository.existsShiftByShiftDateAndUser_Id(LocalDate.parse(shiftRequest.getShiftDate()), shiftRequest.getUserId())){
            throw new DataExistException(ApiErrorMessage.SHIFT_ALREADY_EXISTS_AT_THIS_DAY_FOR_USER_ID.getMessage(shiftRequest.getUserId()));
        }
        Shift shift = shiftMapper.toShift(shiftRequest);


        Project project = projectRepository.findById(shiftRequest.getProjectId())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.PROJECT_NOT_FOUND_BY_ID.getMessage(shiftRequest.getProjectId())));
        shift.setProject(project);

        User user = userRepository.findUserById(shiftRequest.getUserId())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(shiftRequest.getUserId())));

        shift.setUser(user);
        Shift savedShift = shiftRepository.save(shift);

        List<ShiftEmployee> shiftEmployees = new ArrayList<>();

        if(shiftRequest.getEmployeesIds() != null && !shiftRequest.getEmployeesIds().isEmpty()){
            for(Integer empId : shiftRequest.getEmployeesIds()){
                Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new NotFoundException("Employee not found with id: " + empId));

                ShiftEmployee se = new ShiftEmployee();
                se.setShift(savedShift);
                se.setEmployee(employee);
                se.setStatus(Status.PLANNED);

                shiftEmployees.add(se);
            }

        }
        savedShift.setShiftEmployees(shiftEmployees);
        shiftRepository.save(savedShift);
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(savedShift);

//        shiftRepository.save(shift);
//        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);

        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
    public GeneralResponse<ShiftDTO> updateShift(@NotNull Integer id, UpdateShiftRequest updateShiftRequest) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id)));
        shiftMapper.updateShift(shift, updateShiftRequest);

        shift.setShiftDate(LocalDate.parse(updateShiftRequest.getShiftDate()));

        shift = shiftRepository.save(shift);
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);
        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
    public GeneralResponse<PaginationResponse<ShiftDTO>> searchShift(ShiftSearchRequest request, Pageable pageable) {
        Specification<Shift> shiftSpecification = new ShiftSearchCriteria(request);
        Page<ShiftDTO> shifts = shiftRepository.findAll(shiftSpecification, pageable)
                .map(shiftMapper::toShiftDTO);
        PaginationResponse<ShiftDTO> response = PaginationResponse.<ShiftDTO>builder()
                .content(shifts.getContent())
                .pagination(PaginationResponse.Pagination.builder()
                        .total(shifts.getTotalElements())
                        .limit(pageable.getPageSize())
                        .page(shifts.getNumber() + 1)
                        .pages(shifts.getTotalPages())
                        .build())
                .build();
        return GeneralResponse.createSuccessful(response);
    }

    @Transactional
    @Override
    public void deleteShift(@NotNull Integer id, ShiftDateRequest shiftDateRequest) {

        shiftEmployeeRepository.deleteAllByShift_Id(id);

        LocalDate shiftDate = LocalDate.parse(shiftDateRequest.getLocalDate());
        shiftRepository.deleteShiftByUser_IdAndShiftDate(id, shiftDate);
    }
}
