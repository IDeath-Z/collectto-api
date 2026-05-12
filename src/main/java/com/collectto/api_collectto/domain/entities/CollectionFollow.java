package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainValidator;

public class CollectionFollow {

    UUID followerId;
    UUID collectionId;
    Instant createdAt;

    public CollectionFollow(UUID followerId, UUID collectionId, Instant createdAt) {
        this.followerId = DomainValidator.requireNonNull(followerId, "Follower ID cannot be null");
        this.collectionId = DomainValidator.requireNonNull(collectionId, "Collection ID cannot be null");
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Creation timestamp cannot be null");
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
