package com.collectto.api_collectto.application.usecases.itemlike;

import java.util.UUID;

import com.collectto.api_collectto.domain.ports.ItemLikeRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnlikeItemUseCase {

    private final ItemLikeRepository itemLikeRepository;
    private final ItemRepository itemRepository;

    public record Input(UUID itemId, UUID likerId) {}

    public void execute(Input input) {
        if (!itemLikeRepository.existsById(input.itemId(), input.likerId()))
            return;

        itemLikeRepository.deleteById(input.itemId(), input.likerId());
        itemRepository.decrementLikesCount(input.itemId());
    }
}
