package com.collectto.api_collectto.presentation.dto.item;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record ItemResponse(
    @Schema(description = "Unique item identifier", example = "123e4567-e89b-12d3-a456-426614174000") 
    UUID id,
            
    @Schema(description = "Collection identifier", example = "123e4567-e89b-12d3-a456-426614174000") 
    UUID collectionId,
            
    @Schema(description = "User identifier", example = "123e4567-e89b-12d3-a456-426614174000") 
    UUID userId,
            
    @Schema(description = "Item name", example = "Vintage Camera") 
    String name,
            
    @Schema(description = "Item description", example = "A classic film camera from the 1950s") 
    String description,
            
    @Schema(description = "Item acquisition date", example = "2026-01-15") 
    LocalDate acquisitionDate,
            
    @Schema(description = "Item last used date", example = "2026-06-01") 
    LocalDate lastUsedDate,
            
    @Schema(description = "URLs of the item image files", example = "[\"https://example.com/images/item.jpg\"]") 
    List<String> imageFilesUrls,
            
    @Schema(description = "Custom attributes of the item", example = "{\"color\": \"red\", \"size\": \"M\"}") 
    Map<String, Object> attributes,

    @Schema(description = "Number of likes", example = "50")
    int likesCount,

    @Schema(description = "Number of comments", example = "10")
    int commentsCount,
            
    @Schema(description = "Item tags", example = "[\"#tag1\", \"#tag2\"]") 
    List<String> tags,
            
    @Schema(description = "Indicates if the item is active", example = "true") @JsonProperty("isActive") 
    boolean active,
            
    @Schema(description = "Item creation timestamp", example = "2026-01-01T00:00:00Z") 
    Instant createdAt,
            
    @Schema(description = "Item last update timestamp", example = "2026-01-02T00:00:00Z") 
    Instant updatedAt
) {}