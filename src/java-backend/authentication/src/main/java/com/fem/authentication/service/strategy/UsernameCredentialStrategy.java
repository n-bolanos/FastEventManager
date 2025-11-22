package com.fem.authentication.service.strategy;

import com.fem.authentication.dto.LoginRequest;
import com.fem.authentication.entity.User;
import com.fem.authentication.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

/**
 * Username-based credential strategy.
 * Handles identifiers that do not look like emails.
 */
@Component
public class UsernameCredentialStrategy implements CredentialStrategy {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsernameCredentialStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(LoginRequest req) {
        String id = req.identifier();
        return id != null && !id.contains("@");
    }

    @Override
    public Optional<User> authenticate(LoginRequest req) {
        return userRepository
            .findByUsername(req.identifier())
            .filter(u -> passwordEncoder.matches(req.password(), u.getPasswordHash()));
    }
}
