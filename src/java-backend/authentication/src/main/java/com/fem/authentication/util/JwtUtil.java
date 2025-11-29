/**
 * Class responsible for solving common problems related to JWT
 */
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
    private final long expirySecondsAccess;
    private final long expirySecondsRefresh = 86400;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expirySeconds}") Integer expirySeconds){
        this.algorithm = Algorithm.HMAC256(secret);
        this.expirySecondsAccess = (expirySeconds != null) ? expirySeconds : 15000;
    }

    /**
     * Generate a new access token
     * @param subject Subject of the token. Preferably user's id
     * @return short-lived JWT 
     */
    public String generateToken(String subject){
        Date now = new Date();
        return JWT.create()
            .withSubject(subject)
            .withIssuedAt(now)
            .withClaim("type", "access")
            .withExpiresAt(new Date(now.getTime() + expirySecondsAccess * 1000))
            .sign(algorithm);
    }

    /**
     * Generate a new refresh token
     * @param  subject Subject of the token. Preferably user's id
     * @return long-lived JWT
     */
    public String generateRefreshToken(String subject){
        Date now = new Date();
        return JWT.create()
            .withSubject(subject)
            .withIssuedAt(now)
            .withClaim("type", "refresh")
            .withExpiresAt(new Date(now.getTime() + expirySecondsRefresh * 1000))
            .sign(algorithm);
    }

    /**
     * Verifies if a token hasn't expired yet
     * @param token JWT to verify
     * @return Boolean value reporting if JWT is expired.
     */
    public boolean isExpired(String token) {
        Optional<DecodedJWT> decodedOpt = verify(token);
        if (decodedOpt.isEmpty()) {
            return false;
        }
        DecodedJWT decoded = decodedOpt.get();
        return decoded.getExpiresAt().before(new Date());
    }

    /**
     * Verifies the JWT type
     * @param token JWT to verify
     * @return String: "access" or "refresh" depending on the JWT
     */
    public String extractType(String token) {
        return JWT.decode(token).getClaim("type").asString();
    }

    public Optional<DecodedJWT> verify(String token) {
        try {
            return Optional.of(JWT.require(algorithm).build().verify(token));
            
        } catch (SignatureVerificationException exc ) {
            return Optional.empty();
        }
    }
}
