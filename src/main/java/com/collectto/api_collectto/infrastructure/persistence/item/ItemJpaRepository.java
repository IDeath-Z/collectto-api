package com.collectto.api_collectto.infrastructure.persistence.item;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, UUID> {
    
    List<ItemJpaEntity> findByCollectionId(UUID collectionId);
    List<ItemJpaEntity> findByUserId(UUID userId);
    List<ItemJpaEntity> findByCollectionIdAndNameContainingIgnoreCase(UUID collectionId, String name);

    @Query(value = """
        SELECT i.media_urls
        FROM items i
        WHERE i.collection_id = :collectionId
        ORDER BY i.created_at DESC
        LIMIT 3
        """, nativeQuery = true
    )
    List<String> findTop3MediaUrlsByCollectionId(@Param("collectionId") UUID collectionId);
}
