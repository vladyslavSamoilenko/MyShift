package com.example.backend.service.impl;

import com.example.backend.mapper.ShiftMapper;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.Shift;
import com.example.backend.model.enums.Status;
import com.example.backend.model.request.post.shiftRequests.ShiftStatusRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.ShiftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShiftServiceImplTest {
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private ShiftMapper shiftMapper;

    @InjectMocks
    private ShiftServiceImpl shiftService;

    @Test
    void updateShiftStatus_StartShift_SetsActualStartTimeAndStatusPresent() {
        // Arrange
        Integer shiftId = 1;
        Shift mockShift = new Shift();
        mockShift.setId(shiftId);
        mockShift.setStatus(Status.PLANNED);
        mockShift.setActual_start_time(null);

        ShiftStatusRequest request = new ShiftStatusRequest();
        request.setStatus("PRESENT");
        ShiftDTO expectedDto = new ShiftDTO();

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(mockShift));
        when(shiftRepository.save(any(Shift.class))).thenReturn(mockShift);
        when(shiftMapper.toShiftDTO(mockShift)).thenReturn(expectedDto);

        GeneralResponse<ShiftDTO> response = shiftService.updateShiftStatus(shiftId, request);

        assertTrue(response.isSuccess());
        assertNotNull(mockShift.getActual_start_time());
        assertEquals(Status.PRESENT, mockShift.getStatus());
        verify(shiftRepository, times(1)).save(mockShift);
    }

    @Test
    void updateShiftStatus_FinishUnstartedShift_ThrowsIllegalArgumentException() {
        Integer shiftId = 1;
        Shift mockShift = new Shift();
        mockShift.setId(shiftId);
        mockShift.setStatus(Status.PLANNED);
        mockShift.setActual_start_time(null);

        ShiftStatusRequest request = new ShiftStatusRequest();
        request.setStatus("FINISHED");

        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(mockShift));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> shiftService.updateShiftStatus(shiftId, request));

        assertEquals("Cannot finish the shift, shift didnt started", exception.getMessage());
        verify(shiftRepository, never()).save(any());
    }
}
