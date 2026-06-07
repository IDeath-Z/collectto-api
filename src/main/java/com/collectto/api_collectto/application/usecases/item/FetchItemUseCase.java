package com.collectto.api_collectto.application.usecases.item;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchItemUseCase {

    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;
    private final UserFollowRepository userFollowRepository;

   public record Input(UUID itemId, UUID collectionId, UUID requesterId) {}

    public record ItemSummary(UUID id, String name, List<String> imagesURL) {}

    public record Output(UUID id, UUID collectionId, UUID userId, String name, String description, LocalDate acquisitionDate,
        LocalDate lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags, int likesCount, 
        int commentsCount, boolean isActive, Instant createdAt, Instant updatedAt) {}

    public Output execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + input.collectionId()));

        if (!collection.getUserId().equals(input.requesterId())) {
            if (collection.getVisibility() == Visibility.PRIVATE)
                throw new ForbiddenActionException("User does not have permission to access items of this collection");

            if (collection.getVisibility() == Visibility.FRIENDS)
                if (!userFollowRepository.isFollowing(input.requesterId(), collection.getUserId()))
                    throw new ForbiddenActionException("User does not have permission to access items of this collection");
        }

        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + input.itemId()));

        return new Output(
            item.getId(),
            item.getCollectionId(),
            item.getUserId(),
            item.getName(),
            item.getDescription(),
            item.getAcquisitionDate(),
            item.getLastUsedDate(),
            item.getMediaURLs(),
            item.getAttributes(),
            item.getTags(),
            item.getLikesCount(),
            item.getCommentsCount(),
            item.isActive(),
            item.getCreatedAt(),
            item.getUpdatedAt()
        );
    }
}
