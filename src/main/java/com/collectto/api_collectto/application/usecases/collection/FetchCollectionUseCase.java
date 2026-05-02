package com.collectto.api_collectto.application.usecases.collection;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchCollectionUseCase {
    
    private final CollectionRepository collectionRepository;

        public record Input(UUID collectionId, UUID requesterId) {}

    public record Output(UUID id, UUID userId, String name, String description, String coverImageURL, 
        Visibility visibility, int followersCount, List<String> tags, boolean isActive, String createdAt, String updatedAt) {}

    public Output execute(Input input) {

        Collection collection = collectionRepository.findById(input.collectionId())
                .orElseThrow(() -> new RuntimeException("Collection not found")); // Implement proper exception handling as needed

        if (!collection.getUserId().equals(input.requesterId())) {
            if (collection.getVisibility() == Visibility.PRIVATE)
                throw new RuntimeException("Collection is private"); // Implement FRIENDS visibility later, and proper exception handling as needed
        }

        return new Output(
                collection.getId(),
                collection.getUserId(),
                collection.getName(),
                collection.getDescription(),
                collection.getCoverImageUrl(),
                collection.getVisibility(),
                collection.getFollowersCount(),
                collection.getTags(),
                collection.isActive(),
                collection.getCreatedAt().toString(),
                collection.getUpdatedAt().toString()
        );
    }
}
