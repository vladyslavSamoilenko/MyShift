package com.example.backend.service.impl;

import com.example.backend.mapper.ShiftMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.entities.Shift;
import com.example.backend.model.entities.User;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.shiftRequests.ShiftDateRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftSearchRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.ShiftRepository;
import com.example.backend.repository.UserRepository;
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

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public GeneralResponse<ShiftDTO> getById(@NotNull Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id)));
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);
        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
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

        shiftRepository.save(shift);
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);

        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
    public GeneralResponse<ShiftDTO> updateShift(@NotNull Integer id, UpdateShiftRequest updateShiftRequest) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id)));
        shiftMapper.updateShift(shift, updateShiftRequest);
        shift.setUpdatedAt(LocalDateTime.now());
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

//    @Override
//    public GeneralResponse<PaginationResponse<ShiftDTO>> getShiftsByProjectId(Integer id, Pageable pageable) {
////        Optional<List<Shift>> shifts = shiftRepository.findShiftsByProject_Id(id);
////        List<ShiftDTO> listShifts = shifts
////                .orElse(Collections.emptyList())
////                .stream()
////                .map(shiftMapper::toShiftDTO)
////                .toList();
//
//        Page<ShiftDTO> shifts = shiftRepository.findShiftsByProject_Id(id, pageable).map(ShiftMapper::toShiftDTO);
//        return GeneralResponse.createSuccessful(listShifts);
//    }
//
//    @Override
//    public GeneralResponse<PaginationResponse<ShiftDTO>> getShiftsByUserId(Integer id,Pageable pageable) {
//        Optional<List<Shift>> shifts = shiftRepository.findShiftsByUser_Id(id);
//        List<ShiftDTO> listShift = shifts
//                .orElse(Collections.emptyList())
//                .stream()
//                .map(shiftMapper::toShiftDTO)
//                .toList();
//        return GeneralResponse.createSuccessful(listShift);
//    }
//
//    @Override
//    public GeneralResponse<PaginationResponse<ShiftDTO>> getShiftsByProjectIdAndShiftDate(Integer id, ShiftDateRequest shiftDateRequest, Pageable pageable) {
//        Optional<List<Shift>> shifts = shiftRepository.findShiftsByProject_IdAndShiftDate(id, LocalDate.parse(shiftDateRequest.getLocalDate()));
//        List<ShiftDTO> listShift = shifts
//                .orElse(Collections.emptyList())
//                .stream()
//                .map(shiftMapper::toShiftDTO)
//                .toList();
//        return GeneralResponse.createSuccessful(listShift);
//    }

    @Transactional
    @Override
    public void deleteShift(@NotNull Integer id, ShiftDateRequest shiftDateRequest) {
        LocalDate shiftDate = LocalDate.parse(shiftDateRequest.getLocalDate());
        shiftRepository.deleteShiftByUser_IdAndShiftDate(id, shiftDate);
    }
}
