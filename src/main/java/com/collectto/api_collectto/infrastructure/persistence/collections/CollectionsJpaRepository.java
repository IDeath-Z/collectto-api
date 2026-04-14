package com.collectto.api_collectto.infrastructure.persistence.collections;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionsJpaRepository extends JpaRepository<CollectionsJpaEntity, UUID> {
    List<CollectionsJpaEntity> findByUserId(UUID userId);    boolean existsByUserIdAndName(UUID userId, String name);
    List<CollectionsJpaEntity> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name);
}