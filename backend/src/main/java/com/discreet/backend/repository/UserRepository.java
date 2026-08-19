package com.discreet.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.discreet.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // spring reads this method name and automatically writes the SQL:
    // 'SELECT COUNT(*) > 0 FROM users WHERE email = ?'
    boolean existsByEmail(String email);

    // 'SELECT * FROM users WHERE email = ?'
    Optional<User> findByEmail(String email);
}
