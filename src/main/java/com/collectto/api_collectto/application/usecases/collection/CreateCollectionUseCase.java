package com.collectto.api_collectto.application.usecases.collection;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCollectionUseCase {

    private final CollectionRepository collectionsRepository;
    private final StorageProvider storageProvider;
    private final StorageUrlPaths storageUrlPaths;

    public record Input(UUID userId, String name, String description, String coverImageUrl, List<String> tags) {}
    public record Output(UUID id, UUID userId, String name, String description, String coverImageURL, Visibility visibility,
        int followersCount, List<String> tags, boolean isActive, Instant createdAt, Instant updatedAt) {}
                
    public Output execute(Input input) {
        String coverImageUrl = input.coverImageUrl() == null
            ? null
            : storageProvider.buildPublicUrl(input.coverImageUrl());

        if (coverImageUrl != null && !storageUrlPaths.isCollectionPathValid(input.coverImageUrl()))
            throw new BusinessRuleException("Invalid cover image path: " + input.coverImageUrl());

        Collection collection = Collection.createNewCollection(
            input.userId(), 
            input.name(), 
            input.description(), 
            coverImageUrl, 
            input.tags()
        );

        Collection savedCollection = collectionsRepository.save(collection);

        return new Output(
            savedCollection.getId(),
            savedCollection.getUserId(),
            savedCollection.getName(),
            savedCollection.getDescription(),
            savedCollection.getCoverImageUrl(),
            savedCollection.getVisibility(),
            savedCollection.getFollowersCount(),
            savedCollection.getTags(),
            savedCollection.isActive(),
            savedCollection.getCreatedAt(),
            savedCollection.getUpdatedAt()
        );
    }
}
