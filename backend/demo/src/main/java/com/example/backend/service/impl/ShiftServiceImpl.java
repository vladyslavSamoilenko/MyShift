package com.example.backend.service.impl;

import com.example.backend.mapper.ShiftMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.*;
import com.example.backend.model.enums.Status;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.shiftRequests.*;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public GeneralResponse<ShiftDTO> getById(@NotNull Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id)));
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);
        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
    @Transactional
    public GeneralResponse<ShiftDTO> createShift(ShiftRequest shiftRequest) {
        if(shiftRepository.existsShiftByShiftDateAndUser_Id(LocalDate.parse(shiftRequest.getShiftDate()), shiftRequest.getUserId())){
            throw new DataExistException(ApiErrorMessage.SHIFT_ALREADY_EXISTS_AT_THIS_DAY_FOR_USER_ID.getMessage(shiftRequest.getUserId()));
        }

        LocalDate date = LocalDate.parse(shiftRequest.getShiftDate());

        Project project = projectRepository.findById(shiftRequest.getProjectId())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.PROJECT_NOT_FOUND_BY_ID.getMessage(shiftRequest.getProjectId())));

        User manager = userRepository.findUserById(shiftRequest.getUserId())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(shiftRequest.getUserId())));

        Employee employee = employeeRepository.findByIdAndDeletedFalse(shiftRequest.getEmployeeId())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.EMPLOYEE_NOT_FOUND_BY_ID.getMessage(shiftRequest.getEmployeeId())));

        Shift shift = shiftMapper.toShift(shiftRequest);

        shift.setShiftDate(date);
        shift.setProject(project);
        shift.setUser(manager);
        shift.setEmployee(employee);
        shift.setStatus(Status.PLANNED);
        shift.setCreatedAt(LocalDateTime.now());

        Shift savedShift = shiftRepository.save(shift);
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(savedShift);

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
    public GeneralResponse<ShiftDTO> updateShiftStatus(@NotNull Integer id, ShiftStatusRequest shiftStatusRequest) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_DAY.getMessage(id)));

        Status newStatus;
        try{
           newStatus = Status.valueOf(shiftStatusRequest.getStatus());
        }catch (IllegalArgumentException | NullPointerException exception){
            throw new IllegalArgumentException("Invalid status: "+ shiftStatusRequest.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();

        if(newStatus.equals(Status.PRESENT)){
            if(shift.getActual_start_time() != null){
                throw new IllegalArgumentException("Shift already started");
            }
            shift.setActual_start_time(now);
        }

        if (newStatus.equals(Status.FINISHED)){
            if(shift.getActual_start_time() == null){
                throw new IllegalArgumentException("Cannot finish the shift, shift didnt started");
            }
            shift.setActual_end_time(LocalDateTime.now());

            Integer durationInMinutes = Math.toIntExact(Duration.between(shift.getActual_start_time(), now).toMinutes());

            shift.setDuration(durationInMinutes);

        }

        if(newStatus.equals(Status.BREAK_START) && shift.getStatus().equals(Status.PRESENT)){
            if(shift.getActual_start_time() == null){
                throw new IllegalArgumentException("Cannot take a break, the shift wasn't started");
            }

            shift.setBreak_start(LocalTime.from(now));
        }

        if(newStatus.equals(Status.BREAK_END)){
            if (shift.getStatus() != Status.BREAK_START) {
                throw new IllegalArgumentException("Cannot end break. Status is not BREAK_START.");
            }
            if(shift.getBreak_start() == null){
                throw new IllegalArgumentException("Cannot finish a break, the break wasn't started");
            }

            shift.setBreak_end(LocalTime.now());

            Integer durationInMinutes = Math.toIntExact(Duration.between(shift.getBreak_start(), LocalTime.now()).toMinutes());

            Integer totalBreakDuration = shift.getBreak_duration()== null ? 0 : shift.getBreak_duration();
            shift.setBreak_duration(totalBreakDuration + durationInMinutes);
            shift.setBreak_duration(durationInMinutes);
        }

        shift.setStatus(Status.valueOf(shiftStatusRequest.getStatus()));

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
        if (!shiftRepository.existsShiftByShiftDateAndUser_Id(LocalDate.parse(shiftDateRequest.getLocalDate()) ,id)){
            throw new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id));
        }
        LocalDate shiftDate = LocalDate.parse(shiftDateRequest.getLocalDate());
        shiftRepository.deleteShiftByUser_IdAndShiftDate(id, shiftDate);
    }

    @Override
    public void deleteShiftById(@NotNull Integer id) {
        if(!shiftRepository.existsById(id)){
            throw new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id));
        }
        shiftRepository.deleteShiftById(id);
    }

}
