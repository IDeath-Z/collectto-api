package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainValidator;

public class ItemLike {

    private final UUID itemId;
    private final UUID likerId;
    private final Instant createdAt;

    public ItemLike(UUID itemId, UUID likerId, Instant createdAt) {
        this.itemId = DomainValidator.requireNonNull(itemId, "Item ID cannot be null");
        this.likerId = DomainValidator.requireNonNull(likerId, "Liker ID cannot be null");
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Creation timestamp cannot be null");
    }

    public static ItemLike createNewLike(UUID itemId, UUID likerId) {
        return new ItemLike(
            itemId,
            likerId,
            Instant.now()
        );
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getLikerId() {
        return likerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
