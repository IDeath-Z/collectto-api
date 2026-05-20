package com.collectto.api_collectto.presentation.dto.user;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentUserInfoResponse(
    @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000") 
    UUID id,
    
    @Schema(description = "User full name", example = "Maria Silva") 
    String name,
    
    @Schema(description = "Unique username", example = "maria_silva") 
    String username,
    
    @Schema(description = "User email address", example = "maria@email.com", format = "email")
    String email,
    
    @Schema(description = "User bio", example = "Collecting memories")
    String bio,
    
    @Schema(description = "Profile picture URL")
    String profilePictureUrl,
    
    @Schema(description = "Profile background URL")
    String profileBackgroundUrl,
    
    @Schema(description = "Number of followers")
    int followersCount,
    
    @Schema(description = "Number of following")
    int followingCount,
    
    @Schema(description = "User active status")
    boolean isActive,
    
    @Schema(description = "User birthday date", example = "1990-01-01")
    String birthdayDate,
    
    @Schema(description = "Timestamp of user creation", example = "2026-01-01T00:00:00Z")
    String createdAt
) {}
