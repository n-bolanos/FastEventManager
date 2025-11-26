/**
 * Core service responsible for user authentication and registration.
 *
 * Applies the Strategy design pattern via CredentialStrategy
 * to support multiple authentication mechanisms (email+pwd, usr+pwd).
 *
 * AuthService delegates authentication and builds JWT on success.
 */
package com.fem.authentication.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fem.authentication.dto.*;
import com.fem.authentication.entity.User;
import com.fem.authentication.repository.UserRepository;
import com.fem.authentication.service.strategy.CredentialStrategy;
import com.fem.authentication.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final List<CredentialStrategy> credentialStrategies;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EmailKafkaProducer producer;

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
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already in use");
        }
        User u = User.builder()
            .name(req.name())
            .username(req.username())
            .email(req.email())
            .passwordHash(passwordEncoder.encode(req.password()))
            .build();

        userRepository.save(u);

        notifyUser(u.getEmail(), u.getName());
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
        String refresh = jwtUtil.generateRefreshToken(Long.toString(u.getId()));
        return new LoginResponse(token, refresh);
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

    /**
     * Convenient function to send email to a registered user via Pub/sub
     * @param email
     * @param name
     */
    public void notifyUser(String email, String name) {
        EmailRequest request = new EmailRequest(
        email,
        "Welcome to FastEventManager!",
        EmailType.SUCCESSFUL_REGISTER,
        Map.of("name", name)
    );

        producer.sendEmail(request);
    }

    /**
     * Funtion to refresh the access token based on JWT refresh token
     * @param refreshToken JWT
     * @return RefreshResponse indicating: 1) if the token was succesfully refreshed, 2) The new access token
     */
    public RefreshResponse refresh(String refreshToken) {
        if (!"refresh".equals(jwtUtil.extractType(refreshToken))) {
            throw new IllegalArgumentException("Invalid token type. Waiting a refresh token");
        }
        if (jwtUtil.isExpired(refreshToken)) {
            return new RefreshResponse(false, "Refresh token expired");
        }

        DecodedJWT userId = jwtUtil.verify(refreshToken).get();
        String subj = userId.getSubject();

        String newAccess = jwtUtil.generateToken(subj);

        return new RefreshResponse(true, newAccess);
    }

}
