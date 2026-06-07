package com.collectto.api_collectto.presentation.dto.user;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.FollowStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserFollowResponse(
    @Schema(description = "Unique identifier for the follower", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID followerId,

    @Schema(description = "Unique identifier for the followed user", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID followedId,

    @Schema(description = "Status of the follow request, can be PENDING, ACCEPTED or DECLINED", example = "PENDING")
    FollowStatus status,

    @Schema(description = "Timestamp of when the follow request was created", example = "2026-01-01T00:00:00Z")
    Instant createdAt
) {}
