/**
 * Class containing test for the most important functionality of 
 * the authentication micreservice. It includes JWT, internal logic such as
 * registration or login, and the Web layer of SpringBoot
 * 
 */
package com.fem.authentication.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import com.auth0.jwt.exceptions.TokenExpiredException;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class JwtUtilTest {

    @Test
    void testTokenExpires() throws InterruptedException {
    JwtUtil jwtUtil = new JwtUtil("secret123", 2);

    // Create a token that expires quickly
    String token = jwtUtil.generateToken("test");
    assertDoesNotThrow(() -> jwtUtil.verify(token));
    Thread.sleep(3000);
    assertThrows(TokenExpiredException.class, () -> jwtUtil.verify(token));
    }

    @Test
    void testGenerateAndVerifyToken() {
        JwtUtil jwtUtil = new JwtUtil("secret123", null);
        String token = jwtUtil.generateToken("user@example.com");
        assertNotNull(token);
        DecodedJWT decoded = jwtUtil.verify(token).get();       
        assertEquals("user@example.com", decoded.getSubject());
    }

    @Test
    void testErrorForInvalidToken(){
        JwtUtil jwtUtil = new JwtUtil("secret123", null);
        String token = jwtUtil.generateToken("user@example.com");
        token = token.concat("modificacion");
        assertEquals(Optional.empty(), jwtUtil.verify(token));
    }
}