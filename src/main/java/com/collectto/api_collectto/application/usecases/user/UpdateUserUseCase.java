package com.collectto.api_collectto.application.usecases.user;

import java.time.LocalDate;
import java.util.UUID;

import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final StorageProvider storageProvider;
    private final StorageUrlPaths storageUrlPaths;

    public record Input(UUID id, String name, String username, String bio, String profilePictureUrl,
        String profileBackgroundUrl, String birthdayDate) {}       
    public record Output(UUID id, String name, String username, String email, String bio, String profilePictureUrl,
        String profileBackgroundUrl,
        int followersCount, int followingCount, boolean isActive, String birthdayDate, String creationDate) {}

    public Output execute(Input input) {
        User user = userRepository.findById(input.id())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + input.id()));

        String oldPictureUrl = user.getProfilePictureUrl();
        String oldBackgroundUrl = user.getProfileBackgroundUrl();

        String finalPictureUrl = null;
        boolean deleteOldPicture = false;

        String finalBackgroundUrl = null;
        boolean deleteOldBackground = false;


        if (input.profilePictureUrl() != null) {
            if (input.profilePictureUrl().isEmpty()) { // Removes profile picture if empty string is sent
                finalPictureUrl = "";
                deleteOldPicture = true;
            } else if (input.profilePictureUrl().equals(oldPictureUrl)) { // Keeps old picture if the same URL is sent
                finalPictureUrl = oldPictureUrl;
            } else { // Validates and builds URL for new picture
                if (!storageUrlPaths.isProfilePictureValid(input.profilePictureUrl()))
                    throw new BusinessRuleException("Invalid profile image path: " + input.profilePictureUrl());
                finalPictureUrl = storageProvider.buildPublicUrl(input.profilePictureUrl());
                deleteOldPicture = true;
            }
        }

        // Same as above
        if (input.profileBackgroundUrl() != null) {
            if (input.profileBackgroundUrl().isEmpty()) {
                finalBackgroundUrl = "";
                deleteOldBackground = true;
            } else if (input.profileBackgroundUrl().equals(oldBackgroundUrl)) {
                finalBackgroundUrl = oldBackgroundUrl;
            } else {
                if (!storageUrlPaths.isProfileBackgroundValid(input.profileBackgroundUrl()))
                    throw new BusinessRuleException("Invalid background image path: " + input.profileBackgroundUrl());
                finalBackgroundUrl = storageProvider.buildPublicUrl(input.profileBackgroundUrl());
                deleteOldBackground = true;
            }
        }

        User updatedUser = user.updateProfile(
            input.name(), 
            input.username(), 
            input.bio(), 
            finalPictureUrl,
            finalBackgroundUrl, 
            LocalDate.parse(input.birthdayDate())
        );
        
        User savedUser = userRepository.save(updatedUser);

        if (deleteOldPicture && oldPictureUrl != null)
            storageProvider.deleteImage(oldPictureUrl);
        if (deleteOldBackground && oldBackgroundUrl != null)
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
