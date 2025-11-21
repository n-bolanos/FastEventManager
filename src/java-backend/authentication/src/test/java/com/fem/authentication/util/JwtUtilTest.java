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

public class JwtUtilTest {
    /**
     * Tests if JwtUtil generates a valid JWT according to the specified secret
     */
    @Test
    void testGenerateAndVerifyToken() {
        JwtUtil jwtUtil = new JwtUtil("secret123", null);
        String token = jwtUtil.generateToken("user@example.com");
        assertNotNull(token);
        DecodedJWT decoded = jwtUtil.verify(token);
        assertEquals("user@example.com", decoded.getSubject());
    }

    @Test
    void testtestTokenExpires() throws InterruptedException {
    JwtUtil jwtUtil = new JwtUtil("secret123", 1);

    // Create a token that expires quickly
    String token = jwtUtil.generateToken("test");
    assertDoesNotThrow(() -> jwtUtil.verify(token));

    Thread.sleep(2000);

    assertThrows(TokenExpiredException.class, () -> jwtUtil.verify(token));
}

}
