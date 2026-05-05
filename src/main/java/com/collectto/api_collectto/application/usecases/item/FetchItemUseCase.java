package com.collectto.api_collectto.application.usecases.item;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchItemUseCase {

    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;

   public record Input(UUID itemId, UUID collectionId, UUID requesterId) {}

    public record ItemSummary(UUID id, String name, List<String> imagesURL) {}

    public record Output(UUID id, UUID collectionId, UUID userId, String name, String description, String acquisitionDate,
        String lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags, int likesCount, 
        int commentsCount, boolean isActive, String createdAt, String updatedAt) {}

    public Output execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
            .orElseThrow(() -> new RuntimeException("Collection not found")); // Implement better exception handling as needed

        if (!collection.getUserId().equals(input.requesterId()) && collection.getVisibility() == Visibility.PRIVATE)
            throw new RuntimeException("Unauthorized access to private collection"); // Implement better exception handling as needed

        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new RuntimeException("Item not found")); // Implement better exception handling as needed

        return new Output(
            item.getId(),
            item.getCollectionId(),
            item.getUserId(),
            item.getName(),
            item.getDescription(),
            item.getAcquisitionDate().toString(),
            item.getLastUsedDate().toString(),
            item.getMediaURLs(),
            item.getAttributes(),
            item.getTags(),
            item.getLikesCount(),
            item.getCommentsCount(),
            item.isActive(),
            item.getCreatedAt().toString(),
            item.getUpdatedAt().toString()
        );
    }
}
