package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainValidator;

public class ItemComment {

    private final UUID id;
    private final UUID itemId;
    private final UUID authorId;
    private final String content;
    private final Instant createdAt;

    public ItemComment(UUID id, UUID itemId, UUID authorId, String content, Instant createdAt) {
        this.id = DomainValidator.requireNonNull(id, "Comment ID cannot be null");
        this.itemId = DomainValidator.requireNonNull(itemId, "Item ID cannot be null");
        this.authorId = DomainValidator.requireNonNull(authorId, "Author ID cannot be null");
        this.content = DomainValidator.requireNonBlank(content, "Content cannot be null or blank");
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Creation date cannot be null");
    }

    public UUID getId() {
        return id;
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public ItemComment updateContent(String newContent) {
        return new ItemComment(this.id, this.itemId, this.authorId, newContent, this.createdAt);
    }
}
