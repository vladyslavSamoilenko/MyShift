package com.example.backend.repository;

import com.example.backend.model.entities.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    boolean existsByName(String name);
    @EntityGraph(attributePaths = {"shifts", "shifts.user"})
    Optional<Project> findByIdAndDeletedFalse(Integer id);
}
