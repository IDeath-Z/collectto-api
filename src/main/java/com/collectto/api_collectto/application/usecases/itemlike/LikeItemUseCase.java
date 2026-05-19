package com.collectto.api_collectto.application.usecases.itemlike;

import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.ItemLike;
import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemLikeRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeItemUseCase {

    private final ItemLikeRepository itemLikeRepository;
    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID itemId, UUID likerId) {}
    public record Output(UUID itemId, UUID likerId, String createdAt) {}

    public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new RuntimeException("Item not found"));

        Collection collection = collectionRepository.findById(item.getCollectionId())
            .orElseThrow(() -> new RuntimeException("Collection not found"));

        if (!collection.getUserId().equals(input.likerId()) && collection.getVisibility() == Visibility.PRIVATE)
            throw new RuntimeException("Unauthorized access to private collection"); // Implement better exception handling as needed

        if (itemLikeRepository.existsById(input.itemId(), input.likerId()))
            throw new RuntimeException("You already liked this item"); // Implement better exception handling as needed

        ItemLike newLike = ItemLike.createNewLike(
            input.itemId(), 
            input.likerId()
        );

        ItemLike savedLike = itemLikeRepository.save(newLike);
        itemRepository.incrementLikesCount(savedLike.getItemId());

        if (!savedLike.getLikerId().equals(collection.getUserId())) {
            Notification notification = Notification.createItemLikedNotification(
                collection.getUserId(),
                savedLike.getLikerId(),
                savedLike.getItemId()
            );
            notificationRepository.save(notification);
        }

        return new Output(
            savedLike.getItemId(), 
            savedLike.getLikerId(), 
            savedLike.getCreatedAt().toString()
        );
    }
}
