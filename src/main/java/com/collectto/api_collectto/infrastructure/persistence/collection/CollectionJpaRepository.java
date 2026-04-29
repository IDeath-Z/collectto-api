package com.collectto.api_collectto.infrastructure.persistence.collection;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionJpaRepository extends JpaRepository<CollectionJpaEntity, UUID> {
    
    List<CollectionJpaEntity> findByUserId(UUID userId);
    boolean existsByUserIdAndName(UUID userId, String name);
    List<CollectionJpaEntity> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name);
}