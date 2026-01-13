package com.example.backend.controller;

import com.example.backend.model.constants.ApiLogMessage;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.enums.Status;
import com.example.backend.model.request.post.shiftRequests.*;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import com.example.backend.service.ShiftService;
import com.example.backend.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shifts")
public class ShiftsController {

    private final ShiftService shiftService;

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<ShiftDTO>> getShiftById(@PathVariable(name = "id") Integer id){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage());
        GeneralResponse<ShiftDTO> response = shiftService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<ShiftDTO>> createShift(@RequestBody ShiftRequest shiftRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage());
        GeneralResponse<ShiftDTO> response = shiftService.createShift(shiftRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse<ShiftDTO>> updateShift(@PathVariable(name = "id") Integer id,
                                                                 @RequestBody UpdateShiftRequest updateShiftRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage());
        GeneralResponse<ShiftDTO> response = shiftService.updateShift(id, updateShiftRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<ShiftDTO>> deleteShift(@PathVariable Integer id,
                                                                 @RequestBody ShiftDateRequest deleteDateRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());
        shiftService.deleteShift(id, deleteDateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<GeneralResponse<PaginationResponse<ShiftDTO>>> searchShifts(@RequestBody @Valid ShiftSearchRequest request,
                                                                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                      @RequestParam(name = "limit", defaultValue = "10") int limit){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());
        Pageable pageable = PageRequest.of(page,limit);
        GeneralResponse<PaginationResponse<ShiftDTO>> response = shiftService.searchShift(request, pageable);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/updateShiftStatus/{id}")
    public ResponseEntity<GeneralResponse<ShiftDTO>> setShiftStatus(@PathVariable Integer id,
                                                                    @RequestBody ShiftStatusRequest shiftStatusRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<ShiftDTO> shiftDTO = shiftService.updateShiftStatus(id, shiftStatusRequest);
        return ResponseEntity.ok(shiftDTO);
    }
}
