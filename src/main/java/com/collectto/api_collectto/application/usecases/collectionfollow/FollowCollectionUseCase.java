package com.collectto.api_collectto.application.usecases.collectionfollow;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.CollectionFollow;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionFollowRepository;
import com.collectto.api_collectto.domain.ports.CollectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowCollectionUseCase {

    private final CollectionFollowRepository collectionFollowRepository;
    private final CollectionRepository collectionRepository;

    public record Input(UUID followerId, UUID collectionId) {}
    public record Output(UUID followerId, UUID collectionId, String createdAt) {}

    public Output execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
            .orElseThrow(() -> new RuntimeException("Collection not found"));

        if (collection.getUserId().equals(input.followerId()))
            throw new RuntimeException("User cannot follow their own collection"); //Implement better exception handling as needed

        if (collection.getVisibility() == Visibility.PRIVATE)
            throw new RuntimeException("Unauthorized access to private collection"); // Implement better exception handling as needed

        if (collectionFollowRepository.existsById(input.followerId(), input.collectionId()))
            throw new IllegalStateException("You are already following this collection."); // Implement better exception handling as needed

        CollectionFollow newFollow = new CollectionFollow(
            input.followerId(), 
            input.collectionId(), 
            Instant.now()
        );

        CollectionFollow savedFollow = collectionFollowRepository.save(newFollow);
        collectionRepository.incrementFollowers(savedFollow.getCollectionId());

        return new Output(
            savedFollow.getFollowerId(),
            savedFollow.getCollectionId(),
            savedFollow.getCreatedAt().toString()
        );
    }

}
