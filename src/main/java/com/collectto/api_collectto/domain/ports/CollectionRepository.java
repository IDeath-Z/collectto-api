package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface CollectionRepository {
    
    DomainPageResult<Collection> findByUserId(UUID userId, DomainPageRequest pageRequest);
    Collection save(Collection collection);
    Optional<Collection> findById(UUID id);
    List<Collection> findAllByIds(List<UUID> ids);
    List<Collection> findByUserIdAndName(UUID userId, String name);
    DomainPageResult<Collection> findVisibleCollections(UUID userId, UUID requesterId, DomainPageRequest pageRequest);
    void incrementFollowers(UUID collectionId);
    void decrementFollowers(UUID collectionId);
    void deactivateCollection(UUID collectionId);
}
