package com.collectto.api_collectto.application.usecases.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ResourceAlreadyExistsException;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.PasswordHasher;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public record Input(String name, String username, String email, String password, LocalDate birthdayDate) {}
    public record Output(UUID id, String name, String username, String email, String bio, String profilePictureUrl, String profileBackgroundUrl,
        int followersCount, int followingCount, boolean isActive, LocalDate birthdayDate, Instant creationDate) {}
            
    public Output execute(Input input) {
        if (userRepository.existsByEmail(input.email()))
            throw new ResourceAlreadyExistsException("Email already in use");
        if (userRepository.existsByUsername(input.username()))
            throw new ResourceAlreadyExistsException("Username already in use");

        User user = User.createNewUser(
            input.name(),
            input.username(),
            input.email(),
            passwordHasher.hash(input.password()),
            input.birthdayDate()
        );

        User savedUser = userRepository.save(user);

        return new Output(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getBio(),
            savedUser.getProfilePictureUrl(),
            savedUser.getProfileBackgroundUrl(),
            savedUser.getFollowersCount(),
            savedUser.getFollowingCount(),
            savedUser.isActive(),
            savedUser.getBirthdayDate(),
            savedUser.getCreationDate()
        );
    }

}
