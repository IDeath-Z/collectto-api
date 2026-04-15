package com.collectto.api_collectto.presentation.dto.collections;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateCollectionRequest", description = "Payload for creating a new collection")
public record CreateCollectionRequest(
        @NotBlank @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000") 
        UUID userId,
                
        @NotBlank @Schema(description = "Collection name", example = "My Collection") 
        String name,
                
        @NotBlank @Schema(description = "Collection description", example = "A collection of my favorite items") 
        String description,

        @NotBlank @Schema(description = "Collection image", example = "collection_image.jpg") 
        MultipartFile coverImage,

        @NotBlank @Schema(description = "Collection folder", example = "Collections") 
        String folder
) {}
