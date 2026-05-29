package com.collectto.api_collectto.application.usecases.auth;

import com.collectto.api_collectto.application.exceptions.UnauthorizedException;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateTokenUseCase {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public User execute(String token) {
        String validEmail = tokenProvider.validate(token);

        return userRepository.findByEmail(validEmail)
            .orElseThrow(() -> new UnauthorizedException("Invalid session: user not found"));
    }
}
