package com.collectto.api_collectto.application.usecases.user;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class FetchCurrentUserInfoUseCase {

    private final UserRepository userRepository;

    public record Output(UUID id, String username) {}

    public Output execute(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return new Output(
            user.getId(),
            user.getUsername()
        );
    }
}