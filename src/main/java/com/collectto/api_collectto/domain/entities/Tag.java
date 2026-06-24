package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainValidator;

public class Tag {

    private final UUID id;
    private final String name;
    private final int usageCount;
    private final Instant createdAt;

    public Tag(UUID id, String name, int usageCount, Instant createdAt) {
        this.id = DomainValidator.requireNonNull(id, "Tag ID cannot be null");
        this.name = DomainValidator.requireNonNull(name, "Name cannot be null");
        this.usageCount = DomainValidator.requireNonNegative(usageCount, "Usage count cannot be negative");
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Created at timestamp cannot be null");
    }

    public static Tag createNewTag(String name) {
        return new Tag(
            UUID.randomUUID(),
            name,
            0,
            Instant.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
