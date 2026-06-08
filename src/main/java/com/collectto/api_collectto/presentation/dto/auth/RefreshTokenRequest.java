package com.collectto.api_collectto.presentation.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshTokenRequest(
    @Schema(description = "The active refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken
) {}