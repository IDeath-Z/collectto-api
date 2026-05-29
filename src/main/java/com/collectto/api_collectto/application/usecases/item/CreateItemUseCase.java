package com.collectto.api_collectto.application.usecases.item;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateItemUseCase {

    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;
    private final StorageProvider storageProvider;
    private final StorageUrlPaths storageUrlPaths;

    public record Input(UUID collectionId, UUID userId, String name, String description, String acquisitionDate,
        String lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags) {}
            
    public record Output(UUID id, UUID collectionId, UUID userId, String name, String description, String acquisitionDate,
        String lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags, int likesCount, 
        int commentsCount, boolean isActive, String createdAt, String updatedAt) {}
    
    public Output execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + input.collectionId()));

        if (!collection.getUserId().equals(input.userId()))
            throw new ForbiddenActionException("User do not have permission to add items to this collection");

        List<String> imageFilesUrls = (input.imageFilesUrls() == null || input.imageFilesUrls().isEmpty())
            ? null
            : input.imageFilesUrls().stream()
                .filter(path -> {
                    if (!storageUrlPaths.isItemPathValid(path))
                        throw new BusinessRuleException("Invalid image path: " + path);
                    return true;
                })
                .map(storageProvider::buildPublicUrl)
                .toList();

        Item item = Item.createNewItem(
            input.collectionId(),
            input.userId(),
            input.name(),
            input.description(),
            LocalDate.parse(input.acquisitionDate()),
            LocalDate.parse(input.lastUsedDate()),
            imageFilesUrls,
            input.attributes(),
            input.tags()
        );

        Item savedItem = itemRepository.save(item);

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
