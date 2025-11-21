package com.fem.authentication.service.strategy;

import com.fem.authentication.dto.LoginRequest;
import com.fem.authentication.entity.User;
import com.fem.authentication.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

/**
 * Email-based credential strategy.
 * Recognizes identifiers containing '@' and verifies password.
 */
@Component
public class EmailCredentialStrategy implements CredentialStrategy {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public EmailCredentialStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(LoginRequest req) {
        String id = req.identifier();
        return id != null && id.contains("@");
    }

    @Override
    public Optional<User> authenticate(LoginRequest req) {
        return userRepository
            .findByEmail(req.identifier())
            .filter(u -> passwordEncoder.matches(req.password(), u.getPasswordHash()));
    }
}
