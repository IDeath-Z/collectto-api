package com.collectto.api_collectto.infrastructure.persistence.collection;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CollectionJpaRepository extends JpaRepository<CollectionJpaEntity, UUID> {
    
    Page<CollectionJpaEntity> findByUserId(UUID userId, Pageable pageable);
    boolean existsByUserIdAndName(UUID userId, String name);
    List<CollectionJpaEntity> findByUserIdAndNameContainingIgnoreCase(UUID userId, String name);

    @Modifying @Query("UPDATE CollectionJpaEntity c SET c.followersCount = c.followersCount + 1 WHERE c.id = :collectionId")
    void incrementFollowers(UUID collectionId);
    
    @Modifying @Query("UPDATE CollectionJpaEntity c SET c.followersCount = c.followersCount - 1 WHERE c.id = :collectionId")
    void decrementFollowers(UUID collectionId);
}