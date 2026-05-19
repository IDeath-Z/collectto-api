package com.collectto.api_collectto.application.usecases.item;

import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.ports.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteItemUseCase {

    private final ItemRepository itemRepository;

    public record Input(UUID itemId, UUID requesterId) {}

    public void execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new RuntimeException("Item not found with id: " + input.itemId()));

        if (!item.getUserId().equals(input.requesterId()))
            throw new IllegalStateException("Only the owner can delete the item");

        if (!item.isActive())
            throw new IllegalStateException("Item is already deactivated");

        itemRepository.deactivateItem(input.itemId());
    }
}
