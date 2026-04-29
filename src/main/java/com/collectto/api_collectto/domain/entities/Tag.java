package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

public class Tag {

    private final UUID id;
    private final String name;
    private final Instant createdAt;

    public Tag(UUID id, String name, Instant createdAt) {
        if (id == null)
            throw new IllegalArgumentException("User ID is required");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name is required");

        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
