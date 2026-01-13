package com.example.backend.repository;

import com.example.backend.model.entities.Shift;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;


public interface ShiftRepository extends JpaRepository<Shift, Integer>, JpaSpecificationExecutor<Shift> {
    boolean existsById(Integer id);
    boolean existsShiftByShiftDateAndUser_Id(@NotNull LocalDate shiftDate,@NotNull Integer userId);
    void deleteShiftByUser_IdAndShiftDate(Integer userId, LocalDate shiftDate);

}
