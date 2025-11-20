/**
 * REST controller that exposes endpoints.
 *
 * Responsibilities:
 *     - Receive login and registration requests
 *     - Delegate authentication to the Service
 *     - Return JWT tokens upon successful authentication
 *
 * This controller communicates with other microservices using
 * JWT Bearer tokens included in the Authorization header.
 */

package com.fem.authentication.controller;

import com.fem.authentication.dto.*;
import com.fem.authentication.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { 
        this.authService = authService; 
    }

    /**
     * Registers a user for the first time in the system, with name, email, username and password.
     *
     * @param request object containing user information
     * @return a ResponseEntity with status set to CREATED (HTTP 201) if the registration was succesful
     * @throws IllegalArgumentException if the email is aready registered
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Authenticates a user with email/username and password.
     *
     * @param request object containing user credentials (one identifier and the password)
     * @return a ResponseEntity with status set to OK (HTTP 200)
     * @throws IllegalArgumentException if the credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse res = authService.login(req);
        return ResponseEntity.ok(res);
    }

    /**
     * Handles exceptions thrown by other methods in the service
     * 
     * @param ex object of class IllegalArgumentException
     * @return a ResponseEntity with HTTP 409 and a custom message 
     *          if the exception was thrown by any other method
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
