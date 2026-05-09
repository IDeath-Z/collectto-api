package com.collectto.api_collectto.domain.ports;

import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.CollectionFollow;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface CollectionFollowRepository {

    Optional<CollectionFollow> findById(UUID followerId, UUID collectionId);
    CollectionFollow save(CollectionFollow collectionFollow);
    DomainPageResult<CollectionFollow> findByFollowerId(UUID followerId, DomainPageRequest pageRequest);
    DomainPageResult<CollectionFollow> findByCollectionId(UUID collectionId, DomainPageRequest pageRequest);
    boolean existsById(UUID followerId, UUID collectionId);
    void deleteById(UUID followerId, UUID collectionId);
}
