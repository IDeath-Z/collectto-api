package com.collectto.api_collectto.presentation.dto.item;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemLikesPageResponse(
    @Schema(description = "List of users who liked the item")
    List<LikerSummaryResponse> likers,

    @Schema(description = "Total number of pages available")
    int totalPages,

    @Schema(description = "Total number of likes for the item")
    long totalElements,

    @Schema(description = "Current page number")
    int currentPage
) {
    public record LikerSummaryResponse(
        @Schema(description = "Unique identifier of the user who liked the item", example = "123e4567-e89b-12d3-a456-426614174001")
        UUID userId, 

        @Schema(description = "Name of the user who liked the item", example = "John Doe")
        String name, 

        @Schema(description = "Username of the user who liked the item", example = "johndoe")
        String username, 

        @Schema(description = "URL of the user's profile picture", example = "https://...")
        String profilePictureURL
    ) {}
}
