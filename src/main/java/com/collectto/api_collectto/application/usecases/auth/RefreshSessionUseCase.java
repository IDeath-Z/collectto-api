package com.collectto.api_collectto.application.usecases.auth;

import com.collectto.api_collectto.application.exceptions.UnauthorizedException;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.AuthToken;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RefreshSessionUseCase {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthToken execute(String refreshToken) {
        String email = tokenProvider.validateRefreshToken(refreshToken);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!user.isActive()) {
            throw new UnauthorizedException("User account is deactivated");
        }

        return new AuthToken(
            tokenProvider.generateAccessToken(user.getEmail()),
            tokenProvider.generateRefreshToken(user.getEmail())
        );
    }
}