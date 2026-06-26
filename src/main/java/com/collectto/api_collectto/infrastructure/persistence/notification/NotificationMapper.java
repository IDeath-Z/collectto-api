package com.collectto.api_collectto.infrastructure.persistence.notification;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.Notification;

@Component
public final class NotificationMapper {

    public NotificationJpaEntity toJpa(Notification notification) {
        NotificationJpaEntity jpaEntity = new NotificationJpaEntity();
        jpaEntity.setId(notification.getId());
        jpaEntity.setRecipientId(notification.getRecipientId());
        jpaEntity.setActorId(notification.getActorId());
        jpaEntity.setContext(notification.getContext());
        jpaEntity.setReferenceId(notification.getReferenceId());
        jpaEntity.setRead(notification.isRead());
        jpaEntity.setCreatedAt(notification.getCreatedAt());
        return jpaEntity;
    }

    public Notification toDomain(NotificationJpaEntity entity) {
        return new Notification(
            entity.getId(),
            entity.getRecipientId(),
            entity.getActorId(),
            entity.getContext(),
            entity.getReferenceId(),
            entity.isRead(),
            entity.getCreatedAt()
        );
    }
}
