package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

public class CollectionFollow {

    UUID followerId;
    UUID collectionId;
    Instant createdAt;

    public CollectionFollow(UUID followerId, UUID collectionId, Instant createdAt) {
        if (followerId == null || collectionId == null || createdAt == null) {
            throw new IllegalArgumentException("Ids and createdAt cannot be null");
        }

        this.followerId = followerId;
        this.collectionId = collectionId;
        this.createdAt = createdAt;
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
