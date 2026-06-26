package com.collectto.api_collectto.application.usecases.auth;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.AuthToken;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ProcessUserLoginUseCase {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthToken execute(User user) {
        if (!user.isActive()) {
            userRepository.reactivateUser(user.getId());
        }

       return new AuthToken(
            tokenProvider.generateAccessToken(user.getEmail()),
            tokenProvider.generateRefreshToken(user.getEmail())
        );
    }
}
