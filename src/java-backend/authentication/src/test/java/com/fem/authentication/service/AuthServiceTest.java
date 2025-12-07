package com.fem.authentication.service;

import com.fem.authentication.dto.*;
import com.fem.authentication.entity.User;
import com.fem.authentication.repository.UserRepository;
import com.fem.authentication.service.strategy.UsernameCredentialStrategy;
import com.fem.authentication.service.strategy.EmailCredentialStrategy;
import com.fem.authentication.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

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

    @Mock
    private EmailKafkaProducer emailKafkaProducer;

    private AuthService authService;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        
        // Crear la instancia manualmente con el constructor
        authService = new AuthService(
                userRepository,
                jwtUtil,
                List.of(strategy1, strategy2)
        );
        
        // Inyectar el mock del producer usando reflexión
        ReflectionTestUtils.setField(authService, "producer", emailKafkaProducer);
        
        // Configurar el mock para que no haga nada cuando se llame
        doNothing().when(emailKafkaProducer).sendEmail(any(EmailRequest.class));
    }

    //-------------------------- REGISTER TESTS ------------------------------
    @SuppressWarnings("null")
    @Test
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest("Test1", "tst", "testing@mail.com", "12345");

        when(userRepository.existsByEmail(req.email())).thenReturn(false);
        when(userRepository.existsByUsername(req.username())).thenReturn(false);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        authService.register(req);
        verify(userRepository).save(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertEquals(req.name(), saved.getName());
        assertEquals(req.username(), saved.getUsername());
        assertEquals(req.email(), saved.getEmail());
        
        // Verificar que se envió el email
        verify(emailKafkaProducer, times(1)).sendEmail(any(EmailRequest.class));
    }

    @SuppressWarnings("null")
    @Test
    void testPasswrodHashing() {
        RegisterRequest req = new RegisterRequest("Test1", "tst", "testing@mail.com", "12345");

        when(userRepository.existsByEmail(req.email())).thenReturn(false);
        when(userRepository.existsByUsername(req.username())).thenReturn(false);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        authService.register(req);
        verify(userRepository).save(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertNotEquals(req.password(), saved.getPasswordHash());
        assertTrue(saved.getPasswordHash().startsWith("$2a"));
    }

    @SuppressWarnings("null")
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
        verify(emailKafkaProducer, never()).sendEmail(any(EmailRequest.class));
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