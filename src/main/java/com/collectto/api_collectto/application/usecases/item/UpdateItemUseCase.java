package com.collectto.api_collectto.application.usecases.item;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateItemUseCase {
    
    private final ItemRepository itemRepository;
    private final StorageProvider storageProvider;
    private final StorageUrlPaths storageUrlPaths;

    public record Input(UUID itemId, UUID requesterId, String name, String description, LocalDate acquisitionDate, 
        List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags) {}

    public record Output(UUID id, UUID collectionId, UUID userId, String name, String description, String acquisitionDate,
        String lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags, int likesCount, 
        int commentsCount, boolean isActive, String createdAt, String updatedAt) {}

    public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new RuntimeException("Item not found with id: " + input.itemId()));

        if (!item.getUserId().equals(input.requesterId()))
            throw new RuntimeException("Unauthorized");

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
                        throw new RuntimeException("Invalid image path: " + path);
                    }
                    return storageProvider.buildPublicUrl(path);
                })
                .toList();
        }

        Item updatedItem = item.updateItem(input.name(), input.description(), input.acquisitionDate(), processedImageUrls, 
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
            savedItem.getAcquisitionDate().toString(),
            savedItem.getLastUsedDate().toString(), 
            savedItem.getMediaURLs(), 
            savedItem.getAttributes(), 
            savedItem.getTags(), 
            savedItem.getLikesCount(),
            savedItem.getCommentsCount(), 
            savedItem.isActive(), 
            savedItem.getCreatedAt().toString(), 
            savedItem.getUpdatedAt().toString()
        );
    }
}
