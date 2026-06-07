package com.collectto.api_collectto.presentation.dto.item;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateItemRequest", description = "Payload for creating a new item in a collection")
public record CreateItemRequest(
    @NotNull @Schema(description = "ID of the collection to which the item belongs", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID collectionId,
            
    @NotBlank @Schema(description = "Name of the item", example = "Vintage Camera") 
    String name,
            
    @Schema(description = "Description of the item", example = "A classic film camera from the 1950s")
    String description,
            
    @Schema(description = "Date when the item was acquired", example = "2026-01-15")
    LocalDate acquisitionDate,
                
    @Schema(description = "Date when the item was last used", example = "2026-06-01")
    LocalDate lastUsedDate,

    @Schema(description = "List of image file paths associated with the item", example = "[\"items/userId/collectionId/itemId/filename.jpg\"]") 
    List<String> imageFilesUrls,

    @Schema(description = "Custom attributes for the item as key-value pairs", example = "{\"color\": \"red\", \"size\": \"medium\"}")
    Map<String, Object> attributes,
    
    @Schema(description = "List of tags associated with the item", example = "[\"vintage\", \"camera\"]")
    List<String> tags
) {}
