/**
 * Core service responsible for user authentication and registration.
 *
 * Applies the Strategy design pattern via CredentialStrategy
 * to support multiple authentication mechanisms (email+pwd, usr+pwd).
 *
 * AuthService delegates authentication and builds JWT on success.
 */
package com.fem.authentication.service;

import com.fem.authentication.dto.*;
import com.fem.authentication.entity.User;
import com.fem.authentication.repository.UserRepository;
import com.fem.authentication.service.strategy.CredentialStrategy;
import com.fem.authentication.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final List<CredentialStrategy> credentialStrategies;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, 
                        List<CredentialStrategy> credentialStrategies) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.credentialStrategies = credentialStrategies;
    }

    /**
     * Register a new user in the system
     *
     * @param request login request containing email and raw password
     * @throws IllegalArgumentException if the email is already registered
     */
     public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already used");
        }
        User u = User.builder()
            .name(req.name())
            .username(req.username())
            .email(req.email())
            .passwordHash(passwordEncoder.encode(req.password()))
            .build();
        userRepository.save(u);

        //TODO: connect to email micro via pub/sub
    }

    /**
     * Attempts to authenticate a user using the selected credential strategy.
     *
     * @param request login request containing email and raw password
     * @return Authentication response including a JWT token
     * @throws IllegalArgumentException if authentication fails or identifier unsupported
     */
    public LoginResponse login(LoginRequest req) {
        Optional<User> authenticated = credentialStrategies.stream()
            .filter(s -> s.supports(req))
            .findFirst()
            .flatMap(s -> s.authenticate(req));

        User u = authenticated.orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = jwtUtil.generateToken(Long.toString(u.getId()));
        return new LoginResponse(token);
    }

    /**
     * Searches specific user information given thier id
     * 
     * @param req user info request containing the user id
     * @return UserInfoResponse with the desired information
     * @throws IllegalArgumentException if the user doesn't exist
     */
    public UserInfoResponse getUserInfo(UserInfoRequest req) {
        User user = userRepository.findById(req.id())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserInfoResponse(user.getName(), user.getEmail());
    }

    /**
     * Convenience method to get user info by id.
     */
    public UserInfoResponse getUserInfoById(Integer id) {
        return getUserInfo(new UserInfoRequest(id));
    }

}
