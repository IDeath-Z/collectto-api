package com.collectto.api_collectto.presentation.dto.collection;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record CollectionFollowResponse(
    @Schema(description = "Unique identifier for the follower", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID followerId,

    @Schema(description = "Unique identifier for the collection", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID collectionId,
    
    @Schema(description = "Timestamp of when the follow was created", example = "2026-01-01T00:00:00Z")
    Instant createdAt
) {}
