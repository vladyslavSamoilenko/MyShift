package com.example.backend.controller;

import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.service.ShiftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ShiftsController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {com.example.backend.security.model.JwtRequestFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class ShiftControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShiftService shiftService;

    @Test
    void getShiftById_ShouldReturn200AndShiftData() throws Exception {

        Integer shiftId = 1;
        ShiftDTO shiftDTO = new ShiftDTO();
        shiftDTO.setId(shiftId);

        GeneralResponse<ShiftDTO> expectedResponse = GeneralResponse.createSuccessful(shiftDTO);
        when(shiftService.getById(shiftId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/shifts/{id}", shiftId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
