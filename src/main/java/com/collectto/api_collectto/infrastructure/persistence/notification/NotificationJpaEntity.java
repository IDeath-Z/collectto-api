package com.collectto.api_collectto.infrastructure.persistence.notification;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.collectto.api_collectto.domain.enums.NotificationContext;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class NotificationJpaEntity {

    @Id
    @Column(name = "notification_id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;

    @Column(name = "actor_id", nullable = false)
    private UUID actorId;

    @Column(name = "type", nullable = false)
    private NotificationContext context;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "created_at", nullable = false, insertable = false)
    @CreationTimestamp
    private Instant createdAt;
}
