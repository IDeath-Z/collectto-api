package com.collectto.api_collectto.application.usecases.item;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.ports.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteItemUseCase {

    private final ItemRepository itemRepository;

    public record Input(UUID itemId, UUID requesterId) {}

    public void execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + input.itemId()));

        if (!item.getUserId().equals(input.requesterId()))
            throw new ForbiddenActionException("User does not have permission to delete this item");

        itemRepository.deactivateItem(input.itemId());
    }
}
