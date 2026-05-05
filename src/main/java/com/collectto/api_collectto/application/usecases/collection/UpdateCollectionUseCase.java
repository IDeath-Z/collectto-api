package com.collectto.api_collectto.application.usecases.collection;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateCollectionUseCase {

    private final CollectionRepository collectionRepository;
    private final StorageProvider storageProvider;
    private final StorageUrlPaths storageUrlPaths;

    public record Input(UUID id, UUID requesterId, String name, String description, String coverImageUrl, Visibility visibility, List<String> tags) {}

    public record Output(UUID id, UUID userId, String name, String description, String coverImageURL, 
        Visibility visibility, int followersCount, List<String> tags, boolean isActive, String createdAt, String updatedAt) {}

    public Output execute(Input input) {
        Collection collection = collectionRepository.findById(input.id())
            .orElseThrow(() -> new RuntimeException("Collection not found")); // Implement proper exception handling as needed

        if (!collection.getUserId().equals(input.requesterId()))
            throw new RuntimeException("Unauthorized"); // Implement proper exception handling as needed

        String oldCoverImageUrl = collection.getCoverImageUrl();
        String finalCoverImageUrl = null;
        boolean deleteOldCover = false;

        if (input.coverImageUrl() != null) {
            if (input.coverImageUrl().isEmpty()) { // Removes cover image if empty string is sent
                finalCoverImageUrl = "";
                deleteOldCover = true;
            } else if (input.coverImageUrl().equals(oldCoverImageUrl)) { // Keeps old cover if the same URL is sent
                finalCoverImageUrl = oldCoverImageUrl;
            } else { // Validates and builds URL for new cover image
                if (!storageUrlPaths.isCollectionPathValid(input.coverImageUrl()))
                    throw new RuntimeException("Invalid cover image path");
                
                finalCoverImageUrl = storageProvider.buildPublicUrl(input.coverImageUrl());
                deleteOldCover = true;
            }
        }

        Collection updatedCollection = collection.updateCollection(
            input.name(), 
            input.description(), 
            finalCoverImageUrl, 
            input.visibility(), 
            input.tags()
        );
        
        Collection savedCollection = collectionRepository.save(updatedCollection);

        if (deleteOldCover && oldCoverImageUrl != null)
            storageProvider.deleteImage(oldCoverImageUrl);

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
            savedCollection.getCreatedAt().toString(),
            savedCollection.getUpdatedAt().toString()
        );
    }
}