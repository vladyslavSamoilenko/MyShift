package com.example.backend.service.impl;

import com.example.backend.mapper.ShiftMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.entities.Shift;
import com.example.backend.model.entities.User;
import com.example.backend.model.exception.DataExistException;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.ShiftRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ShiftService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public GeneralResponse<ShiftDTO> getById(@NotNull Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.SHIFT_NOT_FOUND_BY_ID.getMessage(id)));
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);
        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
    public GeneralResponse<ShiftDTO> createShift(ShiftRequest shiftRequest) {
        if(shiftRepository.existsShiftByShiftDateAndUser_Id(LocalDate.parse(shiftRequest.getShiftDate(), formatter), shiftRequest.getUserId())){
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
        shift = shiftRepository.save(shift);
        ShiftDTO shiftDTO = shiftMapper.toShiftDTO(shift);
        return GeneralResponse.createSuccessful(shiftDTO);
    }

    @Override
    public GeneralResponse<List<ShiftDTO>> getShiftsByProjectId(Integer id) {
        Optional<List<Shift>> shifts = shiftRepository.findShiftsByProject_Id(id);
        List<ShiftDTO> listShifts = shifts
                .orElse(Collections.emptyList())
                .stream()
                .map(shiftMapper::toShiftDTO)
                .toList();
        return GeneralResponse.createSuccessful(listShifts);
    }

    @Override
    public GeneralResponse<List<ShiftDTO>> getShiftsByUserId(Integer id) {
        Optional<List<Shift>> shifts = shiftRepository.findShiftsByUser_Id(id);
        List<ShiftDTO> listShift = shifts
                .orElse(Collections.emptyList())
                .stream()
                .map(shiftMapper::toShiftDTO)
                .toList();
        return GeneralResponse.createSuccessful(listShift);
    }

    @Override
    public GeneralResponse<List<ShiftDTO>> getShiftsByProjectIdAndShiftDate(Integer id, LocalDate localDate) {
        Optional<List<Shift>> shifts = shiftRepository.findShiftsByProject_IdAndShiftDate(id, localDate);
        List<ShiftDTO> listShift = shifts
                .orElse(Collections.emptyList())
                .stream()
                .map(shiftMapper::toShiftDTO)
                .toList();
        return GeneralResponse.createSuccessful(listShift);
    }

    @Override
    public void deleteShift(Integer id, LocalDate date) {
        shiftRepository.deleteShiftByUser_IdAndShiftDate(id, date);
    }
}
