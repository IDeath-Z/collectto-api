package com.collectto.api_collectto.infrastructure.persistence.item;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, UUID> {
    
    Page<ItemJpaEntity> findByCollectionId(UUID collectionId, Pageable pageable);
    List<ItemJpaEntity> findByUserId(UUID userId);
    List<ItemJpaEntity> findByCollectionIdAndNameContainingIgnoreCase(UUID collectionId, String name);

    @Query(value = """
    SELECT collection_id AS collectionId, media_url AS mediaUrl FROM (
        SELECT 
            i.collection_id,
            i.media_urls[1] AS media_url,
            ROW_NUMBER() OVER(PARTITION BY i.collection_id ORDER BY i.created_at DESC) as rn
        FROM items i
        WHERE i.collection_id IN :collectionIds
          AND i.media_urls IS NOT NULL
          AND array_length(i.media_urls, 1) > 0
    ) AS ranked_items
    WHERE rn <= 3
    """, nativeQuery = true)
    List<CollectionMediaProjection> findTop3MediaUrlsByCollectionIds(@Param("collectionIds") List<UUID> collectionIds);

    @Modifying @Query("UPDATE ItemJpaEntity i SET i.likesCount = i.likesCount + 1 WHERE i.id = :itemId")
    void incrementLikesCount(UUID itemId);

    @Modifying @Query("UPDATE ItemJpaEntity i SET i.likesCount = i.likesCount - 1 WHERE i.id = :itemId")
    void decrementLikesCount(UUID itemId);

    @Modifying @Query("UPDATE ItemJpaEntity i SET i.commentsCount = i.commentsCount + 1 WHERE i.id = :itemId")
    void incrementCommentsCount(UUID itemId);

    @Modifying @Query("UPDATE ItemJpaEntity i SET i.commentsCount = i.commentsCount - 1 WHERE i.id = :itemId")
    void decrementCommentsCount(UUID itemId);
}
