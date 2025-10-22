package com.example.backend.repository;

import com.example.backend.model.entities.Shift;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    boolean existsShiftByShiftDateAndUser_Id(@NotNull LocalDate shiftDate,@NotNull Integer userId);
    Optional<List<Shift>> findShiftsByProject_Id(@NotNull Integer projectId);
    Optional<List<Shift>> findShiftsByUser_Id(@NotNull Integer userId);

    Optional<List<Shift>> findShiftsByProject_IdAndShiftDate(Integer projectId, LocalDate shiftDate);

    void deleteShiftByUser_IdAndShiftDate(@NotNull Integer userId,
                                          @NotNull LocalDate shiftDate);

}
