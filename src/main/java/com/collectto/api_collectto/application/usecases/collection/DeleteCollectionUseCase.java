package com.collectto.api_collectto.application.usecases.collection;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.ports.CollectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteCollectionUseCase {

    private final CollectionRepository collectionRepository;

    public record Input(UUID collectionId, UUID requesterId) {}

    public void execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + input.collectionId()));

        if (!collection.getUserId().equals(input.requesterId()))
            throw new ForbiddenActionException("User does not have permission to delete this collection");

        collectionRepository.deactivateCollection(input.collectionId());
    }
}
