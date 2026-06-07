package com.collectto.api_collectto.presentation.dto.user;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateUserRequest", description = "Request payload for updating user profile")
public record UpdateUserRequest(
    @Schema(description = "User full name", example = "Maria Silva") 
    String name,

    @Schema(description = "Unique username", example = "maria_silva") 
    String username,

    @Schema(description = "User bio", example = "Collecting memories") 
    String bio,

    @Schema(description = "Profile picture path, use null to keep the current image or an empty string to remove the profile picture", example = "profiles/userId/filename.jpg") 
    String profilePictureUrl,

    @Schema(description = "Profile background path, use null to keep the current background or an empty string to remove the profile background", example = "backgrounds/userId/filename.jpg") 
    String profileBackgroundUrl,

    @Schema(description = "User birthday date", example = "1990-01-01") 
    LocalDate birthdayDate
) {}