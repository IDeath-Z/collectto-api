package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.shared.DomainValidator;

public final class Notification {

    private final UUID id;
    private final UUID recipientId;
    private final UUID actorId;
    private final NotificationContext context;
    private final UUID referenceId;
    private final boolean isRead;
    private final Instant createdAt;

    public Notification(UUID id, UUID recipientId, UUID actorId, NotificationContext context, UUID referenceId, boolean isRead, Instant createdAt) {
        this.id = DomainValidator.requireNonNull(id, "Notification ID cannot be null");
        this.recipientId = DomainValidator.requireNonNull(recipientId, "Notification recipient ID cannot be null");
        this.actorId = DomainValidator.requireNonNull(actorId, "Notification actor ID cannot be null");
        this.context = DomainValidator.requireNonNull(context, "Notification context cannot be null");
        this.referenceId = DomainValidator.requireNonNull(referenceId, "Notification reference ID cannot be null");
        this.isRead = isRead;
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Notification created at cannot be null");
    }

    public static Notification createUserFollowRequestNotification(UUID recipientId, UUID actorId) {
        return new Notification(
            UUID.randomUUID(), 
            recipientId, 
            actorId, 
            NotificationContext.USER_FOLLOW_REQUESTED, 
            actorId,
            false, 
            Instant.now()
        );
    }

    public static Notification createCollectionFollowedNotification(UUID recipientId, UUID actorId, UUID referenceId) {
        return new Notification(
            UUID.randomUUID(), 
            recipientId, 
            actorId, 
            NotificationContext.COLLECTION_FOLLOWED, 
            referenceId, 
            false, 
            Instant.now()
        );
    }

    public static Notification createItemCommentedNotification(UUID recipientId, UUID actorId, UUID referenceId) {
        return new Notification(
            UUID.randomUUID(), 
            recipientId, 
            actorId, 
            NotificationContext.ITEM_COMMENTED, 
            referenceId, 
            false, 
            Instant.now()
        );
    }

    public static Notification createItemLikedNotification(UUID recipientId, UUID actorId, UUID referenceId) {
        return new Notification(
            UUID.randomUUID(), 
            recipientId, 
            actorId, 
            NotificationContext.ITEM_LIKED, 
            referenceId, 
            false, 
            Instant.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public UUID getActorId() {
        return actorId;
    }

    public NotificationContext getContext() {
        return context;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public boolean isRead() {
        return isRead;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Notification markAsRead() {
        if (this.isRead) {
            return this;
        }

        return new Notification(
            this.id, 
            this.recipientId, 
            this.actorId, 
            this.context, 
            this.referenceId, 
            true, 
            this.createdAt
        );
    }

    public Notification updateContext(NotificationContext newContext) {
        if (this.context == newContext) {
            return this;
        }

        return new Notification(
            this.id, 
            this.recipientId, 
            this.actorId, 
            newContext, 
            this.referenceId, 
            false, 
            this.createdAt
        );
    }
}
