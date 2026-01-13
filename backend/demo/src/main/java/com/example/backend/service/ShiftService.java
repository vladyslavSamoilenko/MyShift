package com.example.backend.service;

import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.request.post.shiftRequests.*;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;

public interface ShiftService {
    GeneralResponse<ShiftDTO> getById(@NotNull Integer id);

    GeneralResponse<ShiftDTO> createShift(@NotNull ShiftRequest shiftRequest);

    GeneralResponse<ShiftDTO> updateShift(@NotNull Integer id,@NotNull UpdateShiftRequest updateShiftRequest);

    GeneralResponse<ShiftDTO> updateShiftStatus(@NotNull Integer id, @NotNull ShiftStatusRequest shiftStatusRequest);

    GeneralResponse<PaginationResponse<ShiftDTO>> searchShift(@NotNull ShiftSearchRequest request, Pageable pageable);
    void deleteShift(@NotNull Integer id, ShiftDateRequest shiftDateRequest);

}
