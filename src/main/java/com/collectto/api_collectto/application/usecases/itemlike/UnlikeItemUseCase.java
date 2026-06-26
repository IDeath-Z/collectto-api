package com.collectto.api_collectto.application.usecases.itemlike;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.ports.ItemLikeRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UnlikeItemUseCase {

    private final ItemLikeRepository itemLikeRepository;
    private final ItemRepository itemRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID itemId, UUID likerId) {}

    public void execute(Input input) {
        if (!itemLikeRepository.existsById(input.itemId(), input.likerId()))
            throw new ResourceNotFoundException("User has not liked the item with id: " + input.itemId());

        itemLikeRepository.deleteById(input.itemId(), input.likerId());
        itemRepository.decrementLikesCount(input.itemId());
        notificationRepository.deleteByActorIdAndReferenceIdAndContext(
            input.likerId(), 
            input.itemId(), 
            NotificationContext.ITEM_LIKED
        );
    }
}
