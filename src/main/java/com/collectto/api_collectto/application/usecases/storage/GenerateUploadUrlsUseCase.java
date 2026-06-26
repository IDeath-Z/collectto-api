package com.collectto.api_collectto.application.usecases.storage;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.enums.UploadContext;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GenerateUploadUrlsUseCase {

    private final StorageProvider storageProvider;
    private final StorageUrlPaths storageUrlPaths;
    private final int presignedUrlExpirationMinutes;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;

    public record FileInput(String fileName, String contentType) {}
    public record FileOutput(String filePath, String uploadUrl) {}
    public record Input(UUID userId, UUID resourceId, UploadContext context, List<FileInput> files) {}
    public record Output(UUID resourceId, List<FileOutput> files) {}

    public Output execute(Input input) {

        UUID resolvedParentId = null;

        if (input.context() == UploadContext.COLLECTION) {
            Collection collection = collectionRepository.findById(input.resourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with ID: " + input.resourceId()));

            if (!collection.getUserId().equals(input.userId()))
                throw new ForbiddenActionException("User does not have permission to upload files for this collection.");
        }

        if (input.context() == UploadContext.ITEM) {
            Item item = itemRepository.findById(input.resourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + input.resourceId()));

            if (!item.getUserId().equals(input.userId()))
                throw new ForbiddenActionException("User does not have permission to upload files for this item.");
            
            resolvedParentId = item.getCollectionId(); 
        }

        if (input.files() == null || input.files().isEmpty())
            throw new BusinessRuleException("At least one file must be provided for upload.");
        
        if (input.files().size() > 10)
            throw new BusinessRuleException("The maximum limit is 10 files per request.");
        
        if ((input.context() != UploadContext.PROFILE_PICTURE && input.context() != UploadContext.PROFILE_BACKGROUND) && input.resourceId() == null)
                throw new BusinessRuleException("The Resource ID (resourceId) is required for uploads of type " + input.context());

        final UUID finalParentId = resolvedParentId;

        List<FileOutput> results = input.files().stream().map(file -> {
            if (file.contentType() == null || !file.contentType().startsWith("image/"))
                throw new BusinessRuleException("Only image files are allowed. Invalid format: " + file.contentType());

            if (file.fileName() == null || file.fileName().isBlank())
                throw new BusinessRuleException("The file name cannot be empty.");

            String uniqueName = UUID.randomUUID() + "_" + file.fileName().replaceAll("\\s+", "_");
            
            String basePath = switch (input.context()) {
                case PROFILE_PICTURE -> storageUrlPaths.profilePicture() + "/" + input.userId();
                case PROFILE_BACKGROUND -> storageUrlPaths.profileBackground() + "/" + input.userId();
                case COLLECTION -> storageUrlPaths.collectionsPath() + "/" + input.userId() + "/" + input.resourceId();
                case ITEM -> storageUrlPaths.itemsPath() + "/" + input.userId() + "/" + finalParentId + "/" + input.resourceId();
            };
            
            String filePath = basePath + "/" + uniqueName;
            String uploadUrl = storageProvider.generatePresignedUrl(filePath, file.contentType(), presignedUrlExpirationMinutes);
            return new FileOutput(filePath, uploadUrl);
        }).toList();

        return new Output(input.resourceId(), results);
    }
}