package com.collectto.api_collectto.application.usecases.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenerateTokenUseCase {

    private final TokenProvider tokenProvider;

    public String execute(User user) {
       return tokenProvider.generate(user.getEmail(), genExpirationDate());
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-03:00"));
    }
}
