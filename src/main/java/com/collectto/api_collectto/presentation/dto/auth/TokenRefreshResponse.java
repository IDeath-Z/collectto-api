package com.collectto.api_collectto.presentation.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRefreshResponse(
    @Schema(description = "New JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") 
    String accessToken,

    @Schema(description = "New JWT refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") 
    String refreshToken,
            
    @Schema(description = "Token type", example = "Bearer") 
    String tokenType) {

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}