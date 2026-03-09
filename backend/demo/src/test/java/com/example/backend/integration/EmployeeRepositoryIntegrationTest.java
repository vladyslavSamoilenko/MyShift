package com.example.backend.integration;

import com.example.backend.model.entities.Employee;
import com.example.backend.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class EmployeeRepositoryIntegrationTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByIdAndDeletedFalse_WhenEmployeeIsDeleted_ReturnsEmpty() {
        Employee deletedEmployee = new Employee();
        deletedEmployee.setFirstName("Ghost");
        deletedEmployee.setLastName("Worker");
        deletedEmployee.setEmail("ghost@test.com");
        deletedEmployee.setPhone("000000");
        deletedEmployee.setDeleted(true);
        deletedEmployee.setCreatedAt(LocalDateTime.now());
        deletedEmployee.setUpdatedAt(LocalDateTime.now());

        Employee saved = entityManager.persistAndFlush(deletedEmployee);

        Optional<Employee> result = employeeRepository.findByIdAndDeletedFalse(saved.getId());

        assertFalse(result.isPresent(), "Should not find a deleted employee");
    }

    @Test
    void findByIdAndDeletedFalse_WhenEmployeeIsActive_ReturnsEmployee() {
        Employee activeEmployee = new Employee();
        activeEmployee.setFirstName("Active");
        activeEmployee.setLastName("Worker");
        activeEmployee.setEmail("active@test.com");
        activeEmployee.setPhone("111111");
        activeEmployee.setDeleted(false);
        activeEmployee.setCreatedAt(LocalDateTime.now());
        activeEmployee.setUpdatedAt(LocalDateTime.now());

        Employee saved = entityManager.persistAndFlush(activeEmployee);

        Optional<Employee> result = employeeRepository.findByIdAndDeletedFalse(saved.getId());

        assertTrue(result.isPresent(), "Should find an active employee");
    }
}
