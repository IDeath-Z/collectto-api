package com.collectto.api_collectto.application.usecases.collectionfollow;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.ports.CollectionFollowRepository;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnfollowCollectionUseCase {

    private final CollectionFollowRepository collectionFollowRepository;
    private final CollectionRepository collectionRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID followerId, UUID collectionId) {}

    public void execute(Input input) {
        if (!collectionFollowRepository.existsById(input.followerId(), input.collectionId()))
            throw new ResourceNotFoundException("User is not following the collection with id: " + input.collectionId());

        collectionFollowRepository.deleteById(input.followerId(), input.collectionId());
        collectionRepository.decrementFollowers(input.collectionId());
        notificationRepository.deleteByActorIdAndReferenceIdAndContext(
            input.followerId(),
            input.collectionId(),
            NotificationContext.COLLECTION_FOLLOWED
        );
    }
}
