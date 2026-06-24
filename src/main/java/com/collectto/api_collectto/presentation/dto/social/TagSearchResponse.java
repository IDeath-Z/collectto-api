package com.collectto.api_collectto.presentation.dto.social;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagSearchResponse(
    @Schema(description = "List of tags matching the search query")
    List<TagSummaryResponse> content,
    
    @Schema(description = "Total number of pages")
    int totalPages,

    @Schema(description = "Total number of elements")
    long totalElements,
    
    @Schema(description = "Current page number")
    int currentPage
) {
    public record TagSummaryResponse(UUID id, String name, int usageCount) {}
}

