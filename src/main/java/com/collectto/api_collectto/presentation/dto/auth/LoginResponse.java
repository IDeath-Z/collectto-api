package com.collectto.api_collectto.presentation.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") 
    String accessToken,
            
    @Schema(description = "Token type", example = "Bearer") 
    String tokenType) {

    public LoginResponse(String accessToken) {
        this(accessToken, "Bearer");
    }

}