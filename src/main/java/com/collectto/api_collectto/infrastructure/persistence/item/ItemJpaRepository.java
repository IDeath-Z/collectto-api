package com.collectto.api_collectto.infrastructure.persistence.item;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, UUID> {
    
    List<ItemJpaEntity> findByCollectionId(UUID collectionId);
    List<ItemJpaEntity> findByUserId(UUID userId);
    List<ItemJpaEntity> findByCollectionIdAndNameContainingIgnoreCase(UUID collectionId, String name);
}
