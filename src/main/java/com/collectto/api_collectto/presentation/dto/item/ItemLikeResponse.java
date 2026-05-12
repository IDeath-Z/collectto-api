package com.collectto.api_collectto.presentation.dto.item;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemLikeResponse(
    @Schema(description = "Unique identifier of the item liked", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID itemId,

    @Schema(description = "Unique identifier of the user who liked the item", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID likerId,

    @Schema(description = "Timestamp when the like was created", example = "2026-06-01T12:00:00Z")
    String createdAt
) {}
