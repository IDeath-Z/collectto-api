package com.collectto.api_collectto.application.usecases.collection;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchCollectionUseCase {
    
    private final CollectionRepository collectionRepository;
    private final UserFollowRepository userFollowRepository;

    public record Input(UUID collectionId, UUID requesterId) {}

    public record Output(UUID id, UUID userId, String name, String description, String coverImageURL, 
        Visibility visibility, int followersCount, List<String> tags, boolean isActive, Instant createdAt, Instant updatedAt) {}

    public Output execute(Input input) {

        Collection collection = collectionRepository.findById(input.collectionId())
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + input.collectionId()));

        if (!collection.getUserId().equals(input.requesterId())) {
            if (collection.getVisibility() == Visibility.PRIVATE)
                throw new ForbiddenActionException("User does not have permission to access this collection");

            if (collection.getVisibility() == Visibility.FRIENDS)
                if (!userFollowRepository.isFollowing(input.requesterId(), collection.getUserId()))
                    throw new ForbiddenActionException("User does not have permission to access this collection");
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
            collection.getCreatedAt(),
            collection.getUpdatedAt()
        );
    }
}
