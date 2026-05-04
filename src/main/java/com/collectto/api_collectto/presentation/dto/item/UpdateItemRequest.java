package com.collectto.api_collectto.presentation.dto.item;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateItemRequest", description = "Request payload for updating an existing item")
public record UpdateItemRequest(
    @Schema(description = "ID of the item to be updated", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "Name of the item", example = "Vintage Camera")
    String name,

    @Schema(description = "Description of the item", example = "A beautiful vintage camera")
    String description,

    @Schema(description = "Date when the item was acquired", example = "2026-01-01")
    LocalDate acquisitionDate,

    @Schema(description = "List of image URLs associated with the item. Null to keep existing, empty to remove all, or a list of new URLs.", example = "items/userId/collectionId/itemId/newImage.jpg")
    List<String> imageFilesUrls,

    @Schema(description = "Map of attributes for the item", example = "{\"brand\": \"Canon\", \"model\": \"A1\"}")
    Map<String, Object> attributes,

    @Schema(description = "List of tags associated with the item", example = "[\"vintage\", \"camera\"]")
    List<String> tags
) {}
