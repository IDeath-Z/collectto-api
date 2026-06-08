package com.collectto.api_collectto.presentation.dto.auth;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
    @Schema(description = "Unique identifier for the user", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID userId,

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") 
    String accessToken,

    @Schema(description = "JWT refresh token used to get a new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") 
    String refreshToken,
            
    @Schema(description = "Token type", example = "Bearer") 
    String tokenType) {

    public LoginResponse(UUID userId, String accessToken, String refreshToken) {
        this(userId, accessToken, refreshToken, "Bearer");
    }
}