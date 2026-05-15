package com.collectto.api_collectto.domain.ports;

import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface NotificationRepository {

    Optional<Notification> findById(UUID id);
    Notification save(Notification notification);
    DomainPageResult<Notification> findByRecipientId(UUID recipientId, DomainPageRequest pageRequest);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
