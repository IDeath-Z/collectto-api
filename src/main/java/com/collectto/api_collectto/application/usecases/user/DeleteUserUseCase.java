package com.collectto.api_collectto.application.usecases.user;

import java.util.UUID;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public void execute(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!user.isActive())
            throw new IllegalStateException("User is already deactivated");

        User deactivatedUser = user.deactivate();
        userRepository.save(deactivatedUser);
    }
}
