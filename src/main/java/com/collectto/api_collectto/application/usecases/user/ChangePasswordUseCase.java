package com.collectto.api_collectto.application.usecases.user;

import java.util.UUID;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.PasswordHasher;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public record Input(UUID userId, String currentPassword, String newPassword) {}

    public void execute(Input input) {
        User user = userRepository.findById(input.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordHasher.verify(input.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("The current password is incorrect.");
        }

        if (passwordHasher.verify(input.newPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("The new password cannot be the same as the current password.");
        }

        String newHashedPassword = passwordHasher.hash(input.newPassword());
        User updatedUser = user.updatePassword(newHashedPassword);
        userRepository.save(updatedUser);
    }
}
