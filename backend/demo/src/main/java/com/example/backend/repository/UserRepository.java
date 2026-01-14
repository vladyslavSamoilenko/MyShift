package com.example.backend.repository;

import com.example.backend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserById(Integer id);

    Optional<User> findByEmailAndDeletedFalse(String email, boolean deleted);

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    boolean existsUserById(Integer id);
}
