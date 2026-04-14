package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collections;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;

public interface CollectionsRepository {
    List<Collections> findByUserId(UUID userId);
    Collections save(Collections collection);
    Optional<Collections> findById(UUID id);
    List<Collections> findByUserIdAndName(UUID userId, String name);
}
