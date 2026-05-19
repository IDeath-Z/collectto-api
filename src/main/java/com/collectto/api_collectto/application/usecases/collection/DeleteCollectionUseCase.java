package com.collectto.api_collectto.application.usecases.collection;

import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.ports.CollectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteCollectionUseCase {

    private final CollectionRepository collectionRepository;

    public record Input(UUID collectionId, UUID requesterId) {}

    public void execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
            .orElseThrow(() -> new RuntimeException("Collection not found with id: " + input.collectionId()));

        if (!collection.getUserId().equals(input.requesterId()))
            throw new IllegalStateException("Only the owner can delete the collection");

        if (!collection.isActive())
            throw new IllegalStateException("Collection is already deactivated");

        collectionRepository.deactivateCollection(input.collectionId());
    }
}
