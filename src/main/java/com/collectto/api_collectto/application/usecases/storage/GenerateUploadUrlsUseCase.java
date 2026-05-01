package com.collectto.api_collectto.application.usecases.storage;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.UploadContext;
import com.collectto.api_collectto.domain.ports.StorageProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenerateUploadUrlsUseCase {

    private final StorageProvider storageProvider;

    private final String profilePicture;
    private final String profileBackground;
    private final String collectionsPath;
    private final String itemsPath;
    private final int presignedUrlExpirationMinutes;

    public record FileInput(String fileName, String contentType) {}

    public record FileOutput(String filePath, String uploadUrl) {}

    public record Input(UUID userId, UUID resourceId, UUID parentId, UploadContext context, List<FileInput> files) {}

    public record Output(UUID resourceId, List<FileOutput> files) {}

    public Output execute(Input input) {
        List<FileOutput> results = input.files().stream().map(file -> {
            String uniqueName = UUID.randomUUID() + "_" + file.fileName().replaceAll("\\s+", "_");
            String basePath = switch (input.context()) {
                case PROFILE_PICTURE -> profilePicture + "/" + input.userId();
                case PROFILE_BACKGROUND -> profileBackground + "/" + input.userId();
                case COLLECTION -> collectionsPath + "/" + input.userId() + "/" + input.resourceId();
                case ITEM -> itemsPath + "/" + input.userId() + "/" + input.parentId() + "/" + input.resourceId();
            };
            String filePath = basePath + "/" + uniqueName;
            String uploadUrl = storageProvider.generatePresignedUrl(filePath, file.contentType(), presignedUrlExpirationMinutes);
            return new FileOutput(filePath, uploadUrl);
        }).toList();

        return new Output(input.resourceId(), results);
    }


}
