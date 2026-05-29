package com.collectto.api_collectto.application.usecases.itemlike;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceAlreadyExistsException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.ItemLike;
import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemLikeRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeItemUseCase {

    private final ItemLikeRepository itemLikeRepository;
    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;
    private final UserFollowRepository userFollowRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID itemId, UUID likerId) {}
    public record Output(UUID itemId, UUID likerId, String createdAt) {}

    public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + input.itemId()));

        Collection collection = collectionRepository.findById(item.getCollectionId())
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + item.getCollectionId()));

        if (!collection.getUserId().equals(input.likerId())) {
            if (collection.getVisibility() == Visibility.PRIVATE)
                throw new ForbiddenActionException("User does not have permission to like this item");

            if (collection.getVisibility() == Visibility.FRIENDS)
                if (!userFollowRepository.isFollowing(input.likerId(), collection.getUserId()))
                    throw new ForbiddenActionException("User does not have permission to like this item");
        }

        if (itemLikeRepository.existsById(input.itemId(), input.likerId()))
            throw new ResourceAlreadyExistsException("You already liked this item");

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
