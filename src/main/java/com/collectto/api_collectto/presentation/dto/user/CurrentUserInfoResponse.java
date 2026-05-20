package com.collectto.api_collectto.presentation.dto.user;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentUserInfoResponse(
    @Schema(description = "Unique identifier of the logged-in user", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "Username of the logged-in user", example = "john_doe")
    String username
) {}
