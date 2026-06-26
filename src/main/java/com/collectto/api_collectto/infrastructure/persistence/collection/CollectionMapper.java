package com.collectto.api_collectto.infrastructure.persistence.collection;

import java.util.List;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;

@Component
public final class CollectionMapper {

    public CollectionJpaEntity toJpa(Collection collection, UserJpaEntity userJpaEntity) {
        CollectionJpaEntity entity = new CollectionJpaEntity();
        entity.setId(collection.getId());
        entity.setUser(userJpaEntity);
        entity.setName(collection.getName());
        entity.setDescription(collection.getDescription());
        entity.setCoverImageUrl(collection.getCoverImageUrl());
        entity.setVisibility(collection.getVisibility());
        entity.setFollowersCount(collection.getFollowersCount());
        entity.setActive(collection.isActive());
        entity.setCreatedAt(collection.getCreatedAt());
        entity.setUpdatedAt(collection.getUpdatedAt());
        return entity;
    }

    public Collection toDomain(CollectionJpaEntity entity) {
        List<String> tags = entity.getTags().stream()
            .map(TagJpaEntity::getName)
            .toList();

        return new Collection(
            entity.getId(),
            entity.getUser().getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getCoverImageUrl(),
            entity.getVisibility(),
            entity.getFollowersCount(),
            tags,
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
