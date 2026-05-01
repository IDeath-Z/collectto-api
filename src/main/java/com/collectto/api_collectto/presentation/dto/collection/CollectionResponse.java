package com.collectto.api_collectto.presentation.dto.collection;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record CollectionResponse(
        @Schema(description = "Unique collection identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "User identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,
                
        @Schema(description = "Collection name", example = "My Collection")
        String name,
                
        @Schema(description = "Collection description", example = "A collection of my favorite items")
        String description,
                
        @Schema(description = "URL of the collection cover image", example = "https://example.com/images/collection_cover.jpg")
        String coverImageURL,
                
        @Schema(description = "Collection visibility, options are PUBLIC, PRIVATE, FRIENDS", example = "PRIVATE")
        Visibility visibility,
                
        @Schema(description = "Number of followers", example = "100")
        int followersCount,
                        
        @Schema(description = "Collection tags", example = "[\"#tag1\", \"#tag2\"]") 
        List<String> tags,
        
        @Schema(description = "Indicates if the collection is active", example = "true")
        @JsonProperty("isActive")
        boolean active, // Using 'active' as the field name to avoid confusion with 'isActive' in JSON
                
        @Schema(description = "Collection creation timestamp", example = "2023-01-01T00:00:00Z")
        String createdAt,
                
        @Schema(description = "Collection last update timestamp", example = "2023-01-02T00:00:00Z")
        String updatedAt
) {}
