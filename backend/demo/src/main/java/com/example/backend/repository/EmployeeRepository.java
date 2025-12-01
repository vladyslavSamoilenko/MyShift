package com.example.backend.repository;

import com.example.backend.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsEmployeeByEmailAndPhone(String email, String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    Optional<Employee> findByIdAndDeletedFalse(Integer id);
}
