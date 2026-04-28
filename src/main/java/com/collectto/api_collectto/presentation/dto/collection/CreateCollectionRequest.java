package com.collectto.api_collectto.presentation.dto.collection;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateCollectionRequest", description = "Payload for creating a new collection")
public record CreateCollectionRequest(
                
        @NotBlank @Schema(description = "Collection name", example = "My Collection") 
        String name,
                
        @NotBlank @Schema(description = "Collection description", example = "A collection of my favorite items") 
        String description,

        @Schema(description = "Collection cover image path", example = "collections/userId/collectionId/filename.jpg") 
        String coverImageUrl,
                        
        @Schema(description = "Collection tags", example = "[\"#tag1\", \"#tag2\"]") 
        List<String> tags
) {}
