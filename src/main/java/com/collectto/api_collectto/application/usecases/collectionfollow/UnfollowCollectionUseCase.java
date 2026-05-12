package com.collectto.api_collectto.application.usecases.collectionfollow;

import java.util.UUID;

import com.collectto.api_collectto.domain.ports.CollectionFollowRepository;
import com.collectto.api_collectto.domain.ports.CollectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnfollowCollectionUseCase {

    private final CollectionFollowRepository collectionFollowRepository;
    private final CollectionRepository collectionRepository;

    public record Input(UUID followerId, UUID collectionId) {}

    public void execute(Input input) {
        if (!collectionFollowRepository.existsById(input.followerId(), input.collectionId()))
            return; // User is not following the collection, no need to do anything

        collectionFollowRepository.deleteById(input.followerId(), input.collectionId());
        collectionRepository.decrementFollowers(input.collectionId());
    }
}
