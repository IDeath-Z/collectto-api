package com.collectto.api_collectto.presentation.dto.item;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateCommentResponse(
    @Schema(description = "Unique identifier for the comment", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID commentId,

    @Schema(description = "Unique identifier for the item", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID itemId,

    @Schema(description = "Unique identifier for the author", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID authorId,

    @Schema(description = "The message of the comment")
    String content,
    
    @Schema(description = "Timestamp when the comment was created", example = "2026-06-01T12:34:56Z")
    Instant createdAt
) {}
