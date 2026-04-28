package com.collectto.api_collectto.presentation.dto.storage;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record GenerateUploadUrlsResponse(
        @Schema(description = "Unique identifier for the resource, can be a collection ID, item ID, or user ID depending on the context", example = "123e4567-e89b-12d3-a456-426614174000") 
        UUID resourceId,

        @Schema(description = "Generated upload URLs with file paths", example = "[{\"filePath\": \"items/userId/collectionId/pictureUUID.jpg\", \"uploadUrl\": \"https://objectstorage...\"}]") 
        List<FileOutput> files
) {
    public record FileOutput(
            @Schema(description = "Path to reference when creating the resource", example = "items/userId/collectionId/pictureUUID.jpg") 
            String filePath,

            @Schema(description = "Pre-signed URL to PUT the file directly to storage", example = "https://objectstorage...") 
            String uploadUrl) {
    }
}
