package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Item {

    private final UUID id;
    private final UUID collectionId;
    private final UUID userId;
    private final String name;
    private final String description;
    private final LocalDate acquisitionDate;
    private final LocalDate lastUsedDate;
    private final List<String> mediaURLs;
    private final Map<String, Object> attributes;
    private final int likesCount;
    private final int commentsCount;
    private final List<String> tags;
    private final boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Item(UUID id, UUID collectionId, UUID userId, String name, String description, LocalDate aquisitionDate,
            LocalDate lastUsedDate, List<String> mediaURLs, Map<String, Object> attributes, int likesCount,
            int commentsCount, List<String> tags, boolean isActive, Instant createdAt, Instant updatedAt) {
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or blank");

        this.id = id;
        this.collectionId = collectionId;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.acquisitionDate = aquisitionDate;
        this.lastUsedDate = lastUsedDate;
        this.mediaURLs = mediaURLs;
        this.attributes = attributes;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.tags = tags;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCollectionId() {
        return collectionId;
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

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public LocalDate getLastUsedDate() {
        return lastUsedDate;
    }

    public List<String> getMediaURLs() {
        return mediaURLs;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
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

    public Item updateItem(String name, String description, LocalDate acquisitionDate, List<String> mediaURLs, 
                Map<String, Object> attributes, List<String> tags) {
        return new Item(
                this.id,
                this.collectionId,
                this.userId,
                name != null ? name : this.name,
                description != null ? description : this.description,
                acquisitionDate != null ? acquisitionDate : this.acquisitionDate,
                this.lastUsedDate,
                mediaURLs != null ? mediaURLs : this.mediaURLs,
                attributes != null ? attributes : this.attributes,
                this.likesCount,
                this.commentsCount,
                tags != null ? tags : this.tags,
                this.isActive,
                this.createdAt,
                Instant.now());
    }
}
