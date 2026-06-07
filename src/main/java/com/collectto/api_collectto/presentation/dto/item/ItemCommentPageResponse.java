package com.collectto.api_collectto.presentation.dto.item;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemCommentPageResponse(
    @Schema(description = "List of comments for the item")
    List<CommenterSummaryResponse> commenterSummaries,

    @Schema(description = "Total number of pages available")
    int totalPages,

    @Schema(description = "Total number of comments available")
    long totalElements,

    @Schema(description = "Current page number")
    int currentPage
) {
    public record CommenterSummaryResponse(
        @Schema(description = "Unique identifier of the comment", example = "123e4567-e89b-12d3-a456-426614174001")
        UUID commentId,

        @Schema(description = "Unique identifier of the user who made the comment", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "Username of the commenter", example = "john_doe")
        String username, 

        @Schema(description = "URL of the user's profile picture", example = "https://...")
        String profilePictureURL,

        @Schema(description = "Content of the comment", example = "This is a comment.")
        String content,

        @Schema(description = "Timestamp when the comment was created", example = "2026-01-01T12:00:00Z")
        Instant createdAt
    ) {} 
}


