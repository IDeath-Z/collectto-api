package com.collectto.api_collectto.application.usecases.auth;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateTokenUseCase {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public User execute(String token) {
        String valid = tokenProvider.validate(token);

        return userRepository.findByEmail(valid)
            .orElseThrow(() -> new RuntimeException("User not found")); // Implement custom exception later
    }
}
