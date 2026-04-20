package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.Visibility;

public class Collection {
    private final UUID id;
    private final UUID userId;
    private final String name;
    private final String description;
    private final String coverImageUrl;
    private final Visibility visibility;
    private final int followersCount;
    private final List<String> tags;
    private final boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Collection(UUID id, UUID userId, String name, String description, String coverImageUrl,
            Visibility visibility, int followersCount, List<String> tags, boolean isActive, Instant createdAt, Instant updatedAt) {

        if (userId == null)
            throw new IllegalArgumentException("User ID is required");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name is required");

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.visibility = visibility;
        this.followersCount = followersCount;
        this.tags = tags;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
