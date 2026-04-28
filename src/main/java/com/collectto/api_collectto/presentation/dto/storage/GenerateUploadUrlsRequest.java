package com.collectto.api_collectto.presentation.dto.storage;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.UploadContext;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "GenerateUploadUrlsRequest", description = "Request payload for generating pre-signed upload URLs")
public record GenerateUploadUrlsRequest(
        @NotNull @Schema(description = "Unique identifier for the resource, can be a collection ID, item ID, or user ID depending on the context", example = "123e4567-e89b-12d3-a456-426614174000") 
        UUID resourceId,
                
        @Schema(description = "Parent identifier, used only for ITEM context (specify the collection ID)", example = "123e4567-e89b-12d3-a456-426614174000") 
        UUID parentId,
        
        @NotNull @Schema(description = "Context for the upload, options are USER_AVATAR, USER_BANNER, COLLECTION or ITEM", example = "COLLECTION") 
        UploadContext context,
                
        @NotEmpty @Schema(description = "List of files to upload", example = "[{\"fileName\": \"file1.jpg\", \"contentType\": \"image/jpeg\"}, {\"fileName\": \"file2.png\", \"contentType\": \"image/png\"}]") 
        List<FileInput> files
) {
    public record FileInput(
        @NotNull @Schema(description = "Name of the file to upload", example = "file1.jpg") 
        String fileName,
                
        @NotNull @Schema(description = "Content type of the file to upload", example = "image/jpeg") 
        String contentType
    ) {}
}
