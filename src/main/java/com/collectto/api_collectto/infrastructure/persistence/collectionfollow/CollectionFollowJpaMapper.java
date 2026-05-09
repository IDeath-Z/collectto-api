package com.collectto.api_collectto.infrastructure.persistence.collectionfollow;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.CollectionFollow;

@Component
public class CollectionFollowJpaMapper {

    public CollectionFollowJpaEntity toJpa(CollectionFollow collectionFollow) {
        CollectionFollowJpaId id = new CollectionFollowJpaId(collectionFollow.getFollowerId(), collectionFollow.getCollectionId());

        CollectionFollowJpaEntity entity = new CollectionFollowJpaEntity();
        entity.setId(id);
        entity.setCreatedAt(collectionFollow.getCreatedAt());
        return entity;
    }

    public CollectionFollow toDomain(CollectionFollowJpaEntity entity) {
        return new CollectionFollow(
            entity.getId().getFollowerId(),
            entity.getId().getCollectionId(),
            entity.getCreatedAt()
        );
    }
}
