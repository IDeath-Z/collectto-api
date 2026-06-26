package com.collectto.api_collectto.infrastructure.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.collectto.api_collectto.application.exceptions.UnauthorizedException;
import com.collectto.api_collectto.domain.enums.TokenType;
import com.collectto.api_collectto.domain.ports.TokenProvider;

@Component
public final class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final String ISSUER = "collectto_api";

    @Override
    public String generateAccessToken(String subject) {
        Instant expiration = LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00"));
        return generateToken(subject, expiration, TokenType.ACCESS);
    }

    @Override
    public String generateRefreshToken(String subject) {
        Instant expiration = LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.of("-03:00"));
        return generateToken(subject, expiration, TokenType.REFRESH);
    }

    private String generateToken(String subject, Instant expiration, TokenType type) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withClaim("token_type", type.getCode())
                .withExpiresAt(expiration)
                .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating " + type + " token", e);
        }
    }

    @Override
    public String validateAccessToken(String token) {
        return validateToken(token, TokenType.ACCESS);
    }

    @Override
    public String validateRefreshToken(String token) {
        return validateToken(token, TokenType.REFRESH);
    }

    private String validateToken(String token, TokenType expectedType) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withClaim("token_type", expectedType.getCode())
                .build()
                .verify(token);
                
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException("Invalid or expired " + expectedType + " token");
        }
    }
}