package com.collectto.api_collectto.infrastructure.persistence.item;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, UUID> {
    
    Page<ItemJpaEntity> findByCollectionId(UUID collectionId, Pageable pageable);
    List<ItemJpaEntity> findByUserId(UUID userId);
    List<ItemJpaEntity> findByCollectionIdAndNameContainingIgnoreCase(UUID collectionId, String name);

    @Query(value = """
        SELECT i.media_urls[1]
        FROM items i
        WHERE i.collection_id = :collectionId
        AND i.media_urls IS NOT NULL
        AND array_length(i.media_urls, 1) > 0
        ORDER BY i.created_at DESC
        LIMIT 3
        """, nativeQuery = true)
    List<String> findTop3MediaUrlsByCollectionId(@Param("collectionId") UUID collectionId);
}
