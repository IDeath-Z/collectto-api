package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;

public interface CollectionRepository {
    List<Collection> findByUserId(UUID userId);
    Collection save(Collection collection);
    Optional<Collection> findById(UUID id);
    List<Collection> findByUserIdAndName(UUID userId, String name);
}
