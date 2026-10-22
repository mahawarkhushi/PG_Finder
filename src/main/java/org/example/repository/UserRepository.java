package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    // Looks up a user by their email address during login
    Optional<User> findByEmail(String email);

    // Checks if an email is already registered
    boolean existsByEmail(String email);
}