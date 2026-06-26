package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainValidator;

public final class CollectionFollow {

    private final UUID followerId;
    private final UUID collectionId;
    private final Instant createdAt;

    public CollectionFollow(UUID followerId, UUID collectionId, Instant createdAt) {
        this.followerId = DomainValidator.requireNonNull(followerId, "Follower ID cannot be null");
        this.collectionId = DomainValidator.requireNonNull(collectionId, "Collection ID cannot be null");
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Creation timestamp cannot be null");
    }

    public static CollectionFollow createNewFollow(UUID followerId, UUID collectionId) {
        return new CollectionFollow(
            followerId,
            collectionId,
            Instant.now()
        );
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public UUID getCollectionId() {
        return collectionId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
