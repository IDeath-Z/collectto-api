package com.collectto.api_collectto.application.usecases.user;

import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public final class FetchCurrentUserInfoUseCase {

    private final UserRepository userRepository;

        public record Output(UUID id, String name, String username, String email, String bio, String profilePictureUrl,
        String profileBackgroundUrl, int followersCount, int followingCount, boolean isActive, LocalDate birthdayDate, Instant creationDate) {}

    public Output execute(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return new Output(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getEmail(),
            user.getBio(),
            user.getProfilePictureUrl(),
            user.getProfileBackgroundUrl(),
            user.getFollowersCount(),
            user.getFollowingCount(),
            user.isActive(),
            user.getBirthdayDate(),
            user.getCreationDate()
        );
    }
}