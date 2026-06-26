package com.collectto.api_collectto.application.usecases.item;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateItemUseCase {
    
    private final ItemRepository itemRepository;
    private final StorageProvider storageProvider;
    private final StorageUrlPaths storageUrlPaths;

    public record Input(UUID itemId, UUID requesterId, UUID collectionId, String name, String description, LocalDate acquisitionDate, 
        List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags) {}

    public record Output(UUID id, UUID collectionId, UUID userId, String name, String description, LocalDate acquisitionDate,
        LocalDate lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags, int likesCount, 
        int commentsCount, boolean isActive, Instant createdAt, Instant updatedAt) {}

    public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + input.itemId()));

        if (!item.getUserId().equals(input.requesterId()))
            throw new ForbiddenActionException("User does not have permission to update items of this collection");

        List<String> oldImageUrls = item.getMediaURLs() != null ? item.getMediaURLs() : List.of();
        
        List<String> processedImageUrls;
        if (input.imageFilesUrls() == null) { // null keeps the old URLs
            processedImageUrls = null; 
        } else if (input.imageFilesUrls().isEmpty()) { // empty list means the user wants to remove all images
            processedImageUrls = List.of(); 
        } else { // merge old and new URLs, validating new ones and keeping valid old ones
            processedImageUrls = input.imageFilesUrls().stream()
                .map(path -> {
                    if (oldImageUrls.contains(path)) {
                        return path;
                    }
                    if (!storageUrlPaths.isItemPathValid(path)) {
                        throw new BusinessRuleException("Invalid image path: " + path);
                    }
                    return storageProvider.buildPublicUrl(path);
                })
                .toList();
        }

        Item updatedItem = item.updateItem(input.collectionId(), input.name(), input.description(), input.acquisitionDate(), processedImageUrls, 
            input.attributes(), input.tags());
        Item savedItem = itemRepository.save(updatedItem);

        if (input.imageFilesUrls() != null && !oldImageUrls.isEmpty()) {
            for (String oldUrl : oldImageUrls) {
                if (!savedItem.getMediaURLs().contains(oldUrl))
                    storageProvider.deleteImage(oldUrl);
            }
        }

        return new Output(
            savedItem.getId(), 
            savedItem.getCollectionId(), 
            savedItem.getUserId(), 
            savedItem.getName(), 
            savedItem.getDescription(), 
            savedItem.getAcquisitionDate(),
            savedItem.getLastUsedDate(), 
            savedItem.getMediaURLs(), 
            savedItem.getAttributes(), 
            savedItem.getTags(), 
            savedItem.getLikesCount(),
            savedItem.getCommentsCount(), 
            savedItem.isActive(), 
            savedItem.getCreatedAt(), 
            savedItem.getUpdatedAt()
        );
    }
}
