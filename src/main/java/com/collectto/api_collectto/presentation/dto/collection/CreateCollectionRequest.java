package com.collectto.api_collectto.presentation.dto.collection;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateCollectionRequest", description = "Payload for creating a new collection")
public record CreateCollectionRequest(
                
        @NotBlank @Schema(description = "Collection name", example = "My Collection") 
        String name,
                
        @NotBlank @Schema(description = "Collection description", example = "A collection of my favorite items") 
        String description,

        @Schema(description = "Collection image", example = "collection_image.jpg") 
        MultipartFile coverImage,
                        
        @Schema(description = "Collection tags", example = "[\"#tag1\", \"#tag2\"]") 
        List<String> tags
) {}
