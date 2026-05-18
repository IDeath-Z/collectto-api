package com.collectto.api_collectto.infrastructure.persistence.collection;

import java.util.List;
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
    
    Page<CollectionJpaEntity> findByUserId(UUID userId, Pageable pageable);
    boolean existsByUserIdAndName(UUID userId, String name);
    List<CollectionJpaEntity> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name);

    @Query("SELECT c FROM CollectionJpaEntity c WHERE c.user.id = :userId AND (c.user.id = :requesterId OR c.visibility <> :privateVisibility)")
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
        AND c.isActive = true AND c.visibility = :privateVisibility
    """)
    List<CollectionJpaEntity> findRecommendedByTags(
        @Param("tagIds") 
        Set<UUID> tagIds,
        @Param("publicVisibility") Visibility publicVisibility,
        Pageable pageable
    );

    @Query("""
        SELECT DISTINCT c FROM CollectionJpaEntity c 
        LEFT JOIN FETCH c.tags t 
        WHERE c.isActive = true AND c.visibility = :publicVisibility
        ORDER BY c.followersCount DESC
    """)
    List<CollectionJpaEntity> findTrendingCollections(@Param("publicVisibility") Visibility publicVisibility, Pageable pageable);

    @Query("""
        SELECT DISTINCT c FROM CollectionJpaEntity c 
        LEFT JOIN FETCH c.tags t 
        WHERE c.isActive = true AND c.visibility = :publicVisibility
        ORDER BY c.createdAt DESC
    """)
    List<CollectionJpaEntity> findLatestCollections(@Param("publicVisibility") Visibility publicVisibility, Pageable pageable);

    @Query(value = """
        SELECT c.collection_id as collectionId, 
               array_agg(media_url) as mediaUrls
        FROM collections c
        JOIN items i ON c.collection_id = i.item_id
        CROSS JOIN LATERAL unnest(i.media_urls) as media_url
        WHERE c.collection_id IN :collectionIds AND i.is_active = true
        GROUP BY c.collection_id
    """, nativeQuery = true)
    List<CollectionMediaProjection> findMediaForCollections(@Param("collectionIds") Set<UUID> collectionIds);

    @Modifying @Query("UPDATE CollectionJpaEntity c SET c.followersCount = c.followersCount + 1 WHERE c.id = :collectionId")
    void incrementFollowers(UUID collectionId);
    
    @Modifying @Query("UPDATE CollectionJpaEntity c SET c.followersCount = c.followersCount - 1 WHERE c.id = :collectionId")
    void decrementFollowers(UUID collectionId);
}