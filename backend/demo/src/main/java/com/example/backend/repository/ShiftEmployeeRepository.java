package com.example.backend.repository;

import com.example.backend.model.entities.ShiftEmployee;
import com.example.backend.model.entities.ShiftEmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftEmployeeRepository extends JpaRepository<ShiftEmployee, ShiftEmployeeId> {
    List<ShiftEmployee> findAllByShift_Id(Integer shiftId);

    List<ShiftEmployee> findAllByEmployee_Id(Integer employeeId);

    void deleteAllByShift_Id(Integer shiftId);

    boolean existsByShift_IdAndEmployee_Id(Integer shiftId, Integer employeeId);
}
