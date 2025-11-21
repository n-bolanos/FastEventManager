/**
 * Class containing test for the  functionality of the service in 
 * the authentication microservice. It includes register, login and
 * getting the suer information by their id.
 * 
 */
package com.fem.authentication.service;

import com.fem.authentication.dto.*;
import com.fem.authentication.entity.User;
import com.fem.authentication.repository.UserRepository;
import com.fem.authentication.service.AuthService;
import com.fem.authentication.service.strategy.CredentialStrategy;
import com.fem.authentication.service.strategy.UsernameCredentialStrategy;
import com.fem.authentication.service.strategy.EmailCredentialStrategy;
import com.fem.authentication.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsernameCredentialStrategy strategy1;

    @Mock
    private EmailCredentialStrategy strategy2;

    @InjectMocks
    private AuthService authService;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(
                userRepository,
                jwtUtil,
                List.of(strategy1, strategy2)
        );
    }

    //-------------------------- REGISTER TESTS ------------------------------
    @Test
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest("Test1", "tst", "testing@mail.com", "12345");

        when(userRepository.existsByEmail(req.email())).thenReturn(false);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        authService.register(req);
        verify(userRepository).save(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertEquals(req.name(), saved.getName());
        assertEquals(req.username(), saved.getUsername());
        assertEquals(req.email(), saved.getEmail());
    }

        @Test
    void testPasswrodHashing() {
        RegisterRequest req = new RegisterRequest("Test1", "tst", "testing@mail.com", "12345");

        when(userRepository.existsByEmail(req.email())).thenReturn(false);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        authService.register(req);
        verify(userRepository).save(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertNotEquals(req.password(), saved.getPasswordHash());
        assertTrue(saved.getPasswordHash().startsWith("$2a"));
    }

    @Test
    void testRegisterEmailExists() {
        RegisterRequest req = new RegisterRequest("Test1", "tst", "testing@mail.com", "12345");

        when(userRepository.existsByEmail(req.email())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(req)
        );

        assertEquals("Email already used", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    //-------------------------- LOGIN TESTS ------------------------------
    @Test
    void testLoginWithUsernameStrategy(){
        
        LoginRequest req = new LoginRequest("tst", "12345");
        User u = User.builder().name("Test").username(req.identifier())
                               .email("testing@exammple.com").build();

        when(strategy1.supports(req)).thenReturn(true);
        when(strategy1.authenticate(req)).thenReturn(Optional.of(u));

        assertDoesNotThrow(() -> authService.login(req));
        verify(jwtUtil, times(1)).generateToken(any());
    }

    @Test
    void testLoginWithEmailStrategy(){
        
        LoginRequest req = new LoginRequest("testing@example.com", "12345");
        User u = User.builder().name("Test").username("tst")
                               .email(req.identifier()).build();

        when(strategy1.supports(req)).thenReturn(false);
        when(strategy2.supports(req)).thenReturn(true);
        when(strategy2.authenticate(req)).thenReturn(Optional.of(u));

        assertDoesNotThrow(() -> authService.login(req));
        verify(jwtUtil, times(1)).generateToken(any());
    }

}
