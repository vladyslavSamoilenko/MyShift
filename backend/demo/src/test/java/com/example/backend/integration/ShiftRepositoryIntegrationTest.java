package com.example.backend.integration;

import com.example.backend.repository.ShiftRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.entities.Project;
import com.example.backend.model.entities.Shift;
import com.example.backend.model.entities.User;
import com.example.backend.model.enums.Role;
import com.example.backend.model.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class ShiftRepositoryIntegrationTest {
    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void existsShiftByShiftDateAndUser_Id_ReturnsTrueIfShiftExists() {
        Project project = new Project();
        project.setName("DB Test Project");
        project.setDescription("Desc");
        project.setDeleted(false);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        project = entityManager.persistAndFlush(project);

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@test.com");
        employee.setPhone("123");
        employee.setDeleted(false);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee = entityManager.persistAndFlush(employee);

        User user = new User();
        user.setEmail("john@test.com");
        user.setPassword("pass");
        user.setRole(Role.WORKER);
        user.setEmployee(employee);
        user.setProject(project);
        user.setDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user = entityManager.persistAndFlush(user);

        LocalDate targetDate = LocalDate.of(2026, 3, 15);

        Shift shift = new Shift();
        shift.setShiftDate(targetDate);
        shift.setStartTime(LocalTime.of(9, 0));
        shift.setEndTime(LocalTime.of(17, 0));
        shift.setDuration(480);
        shift.setStatus(Status.PLANNED);
        shift.setProject(project);
        shift.setUser(user);
        shift.setEmployee(employee);
        shift.setCreatedAt(LocalDateTime.now());
        entityManager.persistAndFlush(shift);

        boolean exists = shiftRepository.existsShiftByShiftDateAndUser_Id(targetDate, user.getId());

        assertTrue(exists, "Shift should exist for this user and date");

        boolean existsForOtherDate = shiftRepository.existsShiftByShiftDateAndUser_Id(targetDate.plusDays(1), user.getId());
        assertFalse(existsForOtherDate, "Shift should NOT exist for another date");
    }
}
