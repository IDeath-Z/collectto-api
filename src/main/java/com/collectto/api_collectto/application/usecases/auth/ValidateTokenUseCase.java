package com.collectto.api_collectto.application.usecases.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Service
public class ValidateTokenUseCase {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    public User execute(String token) {
        String valid = tokenProvider.validate(token);

        return userRepository.findByEmail(valid)
                .orElseThrow(() -> new RuntimeException("User not found")); // Implement custom exception later
    }
}
