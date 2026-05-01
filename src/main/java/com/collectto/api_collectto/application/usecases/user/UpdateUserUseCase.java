package com.collectto.api_collectto.application.usecases.user;

import java.util.UUID;

import com.collectto.api_collectto.domain.ports.StorageProvider;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final StorageProvider storageProvider;

    public record Input(UUID id, String name, String username, String bio, String profilePictureUrl,
            String profileBackgroundUrl, String birthdayDate) {}       
    public record Output(UUID id, String name, String username, String email, String bio, String profilePictureUrl,
            String profileBackgroundUrl,
            int followersCount, int followingCount, boolean isActive, String birthdayDate, String creationDate) {}

    public Output execute(Input input) {
        User user = userRepository.findById(input.id())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + input.id()));

        if (input.profilePictureUrl() != null && !input.profilePictureUrl().startsWith("avatars/"))
            throw new RuntimeException("Invalid profile image path");
        if (input.profileBackgroundUrl() != null && !input.profileBackgroundUrl().startsWith("banners/"))
            throw new RuntimeException("Invalid background image path");

        String oldPictureUrl = user.getProfilePictureUrl();
        String oldBackgroundUrl = user.getProfileBackgroundUrl();

        String profilePictureUrl = input.profilePictureUrl() == null
                ? null
                : storageProvider.buildPublicUrl(input.profilePictureUrl());
        String profileBackgroundUrl = input.profileBackgroundUrl() == null
                ? null
                : storageProvider.buildPublicUrl(input.profileBackgroundUrl());

        User updatedUser = user.updateProfile(input.name(), input.username(), input.bio(), profilePictureUrl,
                profileBackgroundUrl, input.birthdayDate());
        User savedUser = userRepository.save(updatedUser);

        if (input.profilePictureUrl() != null && oldPictureUrl != null)
            storageProvider.deleteImage(oldPictureUrl);
        if (input.profileBackgroundUrl() != null && oldBackgroundUrl != null)
            storageProvider.deleteImage(oldBackgroundUrl);

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
                savedUser.getBirthdayDate().toString(),
                savedUser.getCreationDate().toString()
        );
    }
}
