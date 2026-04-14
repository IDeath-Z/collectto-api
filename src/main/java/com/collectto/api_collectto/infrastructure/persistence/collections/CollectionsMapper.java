package com.collectto.api_collectto.infrastructure.persistence.collections;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.Collections;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;

@Component
public class CollectionsMapper {

    public CollectionsJpaEntity toJpa(Collections collection, UserJpaEntity userJpaEntity) {
        CollectionsJpaEntity entity = new CollectionsJpaEntity();
        entity.setCollectionId(collection.getId());
        entity.setUser(userJpaEntity);
        entity.setName(collection.getName());
        entity.setDescription(collection.getDescription());
        entity.setCoverImageUrl(collection.getCoverImageUrl());
        entity.setVisibility(collection.getVisibility());
        entity.setFollowersCount(collection.getFollowersCount());
        return entity;
    }

    public Collections toDomain(CollectionsJpaEntity entity) {
        return new Collections(
                entity.getCollectionId(),
                entity.getUser().getUserId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCoverImageUrl(),
                entity.getVisibility(),
                entity.getFollowersCount(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
            );
    }
}
