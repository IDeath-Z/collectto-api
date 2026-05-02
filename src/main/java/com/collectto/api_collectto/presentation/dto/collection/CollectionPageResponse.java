package com.collectto.api_collectto.presentation.dto.collection;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public record CollectionPageResponse(
        @Schema(description = "List of collections")
        List<CollectionSummaryResponse> collections,

        @Schema(description = "Total number of pages")
        int totalPages,

        @Schema(description = "Total number of elements")
        long totalElements,

        @Schema(description = "Current page number")
        int currentPage
) {
    public record CollectionSummaryResponse(
            @Schema(description = "Collection identifier", example = "123e4567-e89b-12d3-a456-426614174000")
            String id,

            @Schema(description = "Collection name", example = "My Collection")
            String name,

            @Schema(description = "Preview of item images", example = "[\"https://...\"]")
            List<String> imagesURL
    ) {}
}