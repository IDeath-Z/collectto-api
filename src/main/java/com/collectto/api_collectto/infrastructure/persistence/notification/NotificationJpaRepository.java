package com.collectto.api_collectto.infrastructure.persistence.notification;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.collectto.api_collectto.domain.enums.NotificationContext;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, UUID> {

    Page<NotificationJpaEntity> findByRecipientId(UUID recipientId, Pageable pageRequest);
    Optional<NotificationJpaEntity> findByRecipientIdAndActorIdAndContext(UUID recipientId, UUID actorId, NotificationContext context);
    void deleteByRecipientIdAndActorIdAndContext(UUID recipientId, UUID actorId, NotificationContext context);
    void deleteByActorIdAndReferenceIdAndContext(UUID actorId, UUID referenceId, NotificationContext context);
}
