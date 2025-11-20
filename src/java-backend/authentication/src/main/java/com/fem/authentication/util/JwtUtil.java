package com.fem.authentication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;


public class JwtUtil {
    private final Algorithm algorithm;
    private final long expirySeconds;

    public JwtUtil(String secret, long expirySeconds){
        this.algorithm = Algorithm.HMAC256(secret);
        this.expirySeconds = expirySeconds;
    }

    public String generateToken(String subject){
        Date now = new Date();
        return JWT.create()
            .withSubject(subject)
            .withIssuedAt(now)
            .withExpiresAt(new Date(now.getTime() + expirySeconds * 1000))
            .sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}
