package com.collectto.api_collectto.application.usecases.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;

@Service
public class GenerateTokenUseCase {

    @Autowired
    private TokenProvider tokenProvider;

    public String execute(User user) {
       return tokenProvider.generate(user.getEmail(), genExpirationDate());
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-03:00"));
    }
}
