package com.fem.authentication.service;

import com.fem.authentication.dto.*;
import com.fem.authentication.entity.User;
import com.fem.authentication.repository.UserRepository;
import com.fem.authentication.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

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

    public LoginResponse login(LoginRequest req) {
        User u = userRepository.findByEmailOrUsername(req.identifier(), req.identifier())
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(u.getId().toString());
        return new LoginResponse(token);
    }

    public UserInfoResponse getUserInfo(UserInfoRequest req){
        User u = userRepository.findById(req.id())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserInfoResponse(u.getName(), u.getEmail());
    }
}
