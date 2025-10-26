package com.example.backend.service;

import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.request.post.shiftRequests.ShiftDateRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
import com.example.backend.model.response.GeneralResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ShiftService {
    GeneralResponse<ShiftDTO> getById(@NotNull Integer id);
    GeneralResponse<ShiftDTO> createShift(@NotNull ShiftRequest shiftRequest);
    GeneralResponse<ShiftDTO> updateShift(@NotNull Integer id,@NotNull UpdateShiftRequest updateShiftRequest);
    GeneralResponse<List<ShiftDTO>> getShiftsByProjectId(@NotNull Integer id);
    GeneralResponse<List<ShiftDTO>> getShiftsByUserId(@NotNull Integer id);
    GeneralResponse<List<ShiftDTO>> getShiftsByProjectIdAndShiftDate(@NotNull Integer id, ShiftDateRequest shiftDateRequest);
    void deleteShift(@NotNull Integer id, ShiftDateRequest shiftDateRequest);

}
