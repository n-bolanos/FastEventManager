package com.fem.authentication.repository;

import com.fem.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(String userID);
    boolean existsById(String userID);
    Optional<User> findByEmailOrUsername(String email, String userID);
    boolean existsByEmailOrUsername(String email, String userID);
}
