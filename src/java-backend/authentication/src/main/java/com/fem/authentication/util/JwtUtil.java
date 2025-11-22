package com.fem.authentication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Algorithm algorithm;
    private final long expirySeconds;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expirySeconds}") Integer expirySeconds){
        this.algorithm = Algorithm.HMAC256(secret);
        this.expirySeconds = (expirySeconds != null) ? expirySeconds : 15000;
    }

    public String generateToken(String subject){
        Date now = new Date();
        return JWT.create()
            .withSubject(subject)
            .withIssuedAt(now)
            .withExpiresAt(new Date(now.getTime() + expirySeconds * 1000))
            .sign(algorithm);
    }

    public Optional<DecodedJWT> verify(String token) {
        try {
            return Optional.of(JWT.require(algorithm).build().verify(token));
            
        } catch (SignatureVerificationException exc ) {
            return Optional.empty();
        }
    }
}
