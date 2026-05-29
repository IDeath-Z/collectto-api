package com.collectto.api_collectto.infrastructure.persistence.collection;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.infrastructure.persistence.item.CollectionMediaProjection;

public interface CollectionJpaRepository extends JpaRepository<CollectionJpaEntity, UUID> {
    
    Optional<CollectionJpaEntity> findByIdAndIsActiveTrue(UUID id);
    Page<CollectionJpaEntity> findByUserIdAndIsActiveTrue(UUID userId, Pageable pageable);
    boolean existsByUserIdAndNameAndIsActiveTrue(UUID userId, String name);
    List<CollectionJpaEntity> findByUserIdAndNameContainingIgnoreCaseAndIsActiveTrue(UUID userId, String name);

    @Query("""
        SELECT c FROM CollectionJpaEntity c 
        WHERE c.isActive = true 
        AND c.user.isActive = true
        AND c.visibility = :publicVisibility 
        AND (c.name ILIKE %:query% OR c.description ILIKE %:query%)
    """)
    Page<CollectionJpaEntity> searchPublicCollections(
            @Param("query") String query, 
            @Param("publicVisibility") Visibility publicVisibility, 
            Pageable pageable
        );
    
    @Query("""
        SELECT c FROM CollectionJpaEntity c 
        WHERE c.isActive = true
        AND c.user.id = :userId 
        AND (c.user.id = :requesterId OR c.visibility <> :privateVisibility)
    """)
    Page<CollectionJpaEntity> findVisibleCollections(
        @Param("userId") UUID userId, 
        @Param("requesterId") UUID requesterId, 
        @Param("privateVisibility") Visibility privateVisibility, 
        Pageable pageable
    );

    @Query("""
        SELECT DISTINCT c FROM CollectionJpaEntity c 
        JOIN FETCH c.tags t 
        WHERE t.id IN :tagIds 
        AND c.isActive = true 
        AND c.user.isActive = true
        AND c.visibility = :publicVisibility
        AND c.user.id != :requesterId 
    """)
    List<CollectionJpaEntity> findRecommendedByTags(
        @Param("tagIds") Set<UUID> tagIds,
        @Param("publicVisibility") Visibility publicVisibility,
        @Param("requesterId") UUID requesterId,
        Pageable pageable
    );

    @Query("""
        SELECT DISTINCT c FROM CollectionJpaEntity c 
        LEFT JOIN FETCH c.tags t 
        WHERE c.isActive = true 
        AND c.user.isActive = true
        AND c.visibility = :publicVisibility
        AND c.user.id != :requesterId 
        ORDER BY c.followersCount DESC
    """)
    List<CollectionJpaEntity> findTrendingCollections(
        @Param("publicVisibility") Visibility publicVisibility, 
        @Param("requesterId") UUID requesterId,
        Pageable pageable
    );

    @Query("""
        SELECT DISTINCT c FROM CollectionJpaEntity c 
        LEFT JOIN FETCH c.tags t 
        WHERE c.isActive = true 
        AND c.user.isActive = true
        AND c.visibility = :publicVisibility
        AND c.user.id != :requesterId 
        ORDER BY c.createdAt DESC
    """)
    List<CollectionJpaEntity> findLatestCollections(
        @Param("publicVisibility") Visibility publicVisibility, 
        @Param("requesterId") UUID requesterId,
        Pageable pageable
    );

    @Query(value = """
        SELECT c.id as collectionId, 
               (array_agg(media_url ORDER BY i.created_at DESC))[1:3] as mediaUrls
        FROM collections c
        JOIN items i ON c.id = i.collection_id
        CROSS JOIN LATERAL unnest(i.media_urls) as media_url
        WHERE c.id IN :collectionIds 
        AND c.is_active = true
        AND i.is_active = true
        GROUP BY c.id
    """, nativeQuery = true)
    List<CollectionMediaProjection> findMediaForCollections(@Param("collectionIds") Set<UUID> collectionIds);

    @Modifying @Query("UPDATE CollectionJpaEntity c SET c.followersCount = c.followersCount + 1 WHERE c.id = :collectionId")
    void incrementFollowers(UUID collectionId);
    
    @Modifying @Query("UPDATE CollectionJpaEntity c SET c.followersCount = c.followersCount - 1 WHERE c.id = :collectionId")
    void decrementFollowers(UUID collectionId);

    @Modifying @Query("UPDATE CollectionJpaEntity c SET c.isActive = false, c.deactivatedAt = CURRENT_TIMESTAMP WHERE c.id = :collectionId")
    void deactivateCollection(@Param("collectionId") UUID collectionId);

    @Modifying @Query(value = """
        DELETE FROM collections 
        WHERE is_active = false 
        AND deactivated_at <= :cutoffDate
    """, nativeQuery = true)
    int purgeOldDeactivatedCollections(@Param("cutoffDate") Instant cutoffDate);

    @Query(value = """
        SELECT * FROM collections 
        WHERE is_active = false 
        AND deactivated_at <= :cutoffDate
    """, nativeQuery = true)
    List<CollectionJpaEntity> findCollectionsToPurge(@Param("cutoffDate") Instant cutoffDate);
}