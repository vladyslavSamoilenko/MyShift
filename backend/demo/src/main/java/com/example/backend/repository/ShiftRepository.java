package com.example.backend.repository;

import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.Shift;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.model.response.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Integer>, JpaSpecificationExecutor<Shift> {
    boolean existsShiftByShiftDateAndUser_Id(@NotNull LocalDate shiftDate,@NotNull Integer userId);
//    Optional<Shift> findShiftsByProject_Id(@NotNull Integer projectId);
//    Optional<Shift> findShiftsByUser_Id(@NotNull Integer userId);
//
//    Optional<Shift> findShiftsByProject_IdAndShiftDate(Integer projectId, LocalDate shiftDate);
//    GeneralResponse<PaginationResponse<ShiftDTO>> fi
    void deleteShiftByUser_IdAndShiftDate(Integer userId, LocalDate shiftDate);

}
