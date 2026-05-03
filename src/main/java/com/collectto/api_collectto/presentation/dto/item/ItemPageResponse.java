package com.collectto.api_collectto.presentation.dto.item;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemPageResponse(
    @Schema(description = "List of items in the collection")
    List<ItemSummaryResponse> items,

    @Schema(description = "Total number of pages")
    int totalPages,

    @Schema(description = "Total number of elements")
    long totalElements,
    
    @Schema(description = "Current page number")
    int currentPage

) {
    public record ItemSummaryResponse(
        @Schema(description = "Unique identifier of the item", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Name of the item", example = "Vintage Camera")
        String name,
        @Schema(description = "Preview of items images, limited to 3", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        List<String> imagesURL
    ) {}
}
