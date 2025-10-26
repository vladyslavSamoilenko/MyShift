package com.example.backend.controller;

import com.example.backend.model.constants.ApiLogMessage;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.request.post.shiftRequests.ShiftDateRequest;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<GeneralResponse<ShiftDTO>> updateShift(@PathVariable(name = "id") Integer id,@RequestBody UpdateShiftRequest updateShiftRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage());
        GeneralResponse<ShiftDTO> response = shiftService.updateShift(id, updateShiftRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<ShiftDTO>> deleteShift(@PathVariable Integer id, @RequestBody ShiftDateRequest deleteDateRequest) {
        shiftService.deleteShift(id, deleteDateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/projectId/{id}")
    public ResponseEntity<GeneralResponse<List<ShiftDTO>>> getShiftsByProjectId(@PathVariable(name = "id") Integer id){
        GeneralResponse<List<ShiftDTO>> response = shiftService.getShiftsByProjectId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/userId/{id}")
    public ResponseEntity<GeneralResponse<List<ShiftDTO>>> getShiftsByUserId(@PathVariable(name = "id") Integer id){
        GeneralResponse<List<ShiftDTO>> response = shiftService.getShiftsByUserId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/ProjectIdAndDate/{id}")
    public ResponseEntity<GeneralResponse<List<ShiftDTO>>> getShiftsByProjectIdAndShiftDate(@PathVariable(name = "id") Integer id,@RequestBody ShiftDateRequest shiftDateRequest){
        GeneralResponse<List<ShiftDTO>> response = shiftService.getShiftsByProjectIdAndShiftDate(id,shiftDateRequest);
        return ResponseEntity.ok(response);
    }
}
