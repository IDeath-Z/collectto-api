package com.collectto.api_collectto.presentation.dto.collection;

import java.util.List;

import com.collectto.api_collectto.domain.enums.Visibility;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateCollectionRequest", description = "Request Payload for updating a collection")
public record UpdateCollectionRequest(
    @Schema(description = "Name of the collection", example = "My Updated Collection")
    String name,

    @Schema(description = "Description of the collection", example = "This is an updated description for my collection.")
    String description,

    @Schema(description = "URL of the new cover image for the collection, use null to keep the current image or an empty string to remove the cover image", example = "collections/userId/collectionId/new-cover.jpg")
    String coverImageUrl,

    @Schema(description = "Visibility of the collection, can be PUBLIC, PRIVATE or FRIENDS", example = "PUBLIC")
    Visibility visibility,

    @Schema(description = "List of tags associated with the collection", example = "[\"tag1\", \"tag2\"]")
    List<String> tags
) {}
