package com.example.backend.service;

import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.request.post.shiftRequests.ShiftDateRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftSearchRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;

public interface ShiftService {
    GeneralResponse<ShiftDTO> getById(@NotNull Integer id);

    GeneralResponse<ShiftDTO> createShift(@NotNull ShiftRequest shiftRequest);

    GeneralResponse<ShiftDTO> updateShift(@NotNull Integer id,@NotNull UpdateShiftRequest updateShiftRequest);

    GeneralResponse<PaginationResponse<ShiftDTO>> searchShift(@NotNull ShiftSearchRequest request, Pageable pageable);
    void deleteShift(@NotNull Integer id, ShiftDateRequest shiftDateRequest);

}
