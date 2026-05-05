package com.collectto.api_collectto.infrastructure.auth;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.collectto.api_collectto.domain.ports.TokenProvider;

@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public String generate(String subject, Instant expiration) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                .withIssuer("collectto_api") // Consider making this configurable later
                .withSubject(subject)
                .withExpiresAt(expiration)
                .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating token", e); // Implement proper exception handling later
        }
    }

    @Override
    public String validate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.require(algorithm)
                .withIssuer("collectto_api") // Same as above
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired token", e); // Implement proper exception handling later
        }
    }

}
