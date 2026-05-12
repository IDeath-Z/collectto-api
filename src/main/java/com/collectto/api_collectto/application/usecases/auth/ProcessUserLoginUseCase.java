package com.collectto.api_collectto.application.usecases.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProcessUserLoginUseCase {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public String execute(User user) {
        if (!user.isActive()) {
            User activatedUser = user.activate();
            userRepository.save(activatedUser);
        }

       return tokenProvider.generate(user.getEmail(), genExpirationDate());
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-03:00"));
    }
}
