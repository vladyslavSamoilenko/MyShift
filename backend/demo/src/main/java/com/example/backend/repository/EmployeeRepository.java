package com.example.backend.repository;

import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.response.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsEmployeeByEmailAndPhone(String email, String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    Optional<Employee> findByIdAndDeletedFalse(Integer id);

    @Query("SELECT u.employee FROM User u WHERE u.project.id = :projectId AND u.employee.deleted = false")
    Page<Employee> findAllEmployeesByProjectId(@Param("projectId") Integer projectId, Pageable pageable);
}
