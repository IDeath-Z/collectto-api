package com.collectto.api_collectto.application.usecases.storage;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.collectto.api_collectto.domain.enums.UploadContext;
import com.collectto.api_collectto.domain.ports.StorageProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenerateUploadUrlsUseCase {

    @Value("${storage.path.avatars}")
    private String avatarsPath;

    @Value("${storage.path.banners}")
    private String bannersPath;

    @Value("${storage.path.collections}")
    private String collectionsPath;

    @Value("${storage.path.items}")
    private String itemsPath;

    @Value("${storage.presignedUrlExpirationMinutes}")
    private int presignedUrlExpirationMinutes;

    private final StorageProvider storageProvider;

    public record FileInput(String fileName, String contentType) {}

    public record FileOutput(String filePath, String uploadUrl) {}

    public record Input(UUID userId, UUID resourceId, UUID parentId, UploadContext context, List<FileInput> files) {}

    public record Output(UUID resourceId, List<FileOutput> files) {}

    public Output execute(Input input) {
        List<FileOutput> results = input.files().stream().map(file -> {
            String uniqueName = UUID.randomUUID() + "_" + file.fileName().replaceAll("\\s+", "_");
            String basePath = switch (input.context()) {
                case USER_AVATAR -> avatarsPath + "/" + input.userId();
                case USER_BANNER -> bannersPath + "/" + input.userId();
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
