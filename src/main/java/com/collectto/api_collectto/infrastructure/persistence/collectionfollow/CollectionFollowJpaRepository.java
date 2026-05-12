package com.collectto.api_collectto.infrastructure.persistence.collectionfollow;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CollectionFollowJpaRepository extends JpaRepository<CollectionFollowJpaEntity, CollectionFollowJpaId> {

    @Query("SELECT cf FROM CollectionFollowJpaEntity cf WHERE cf.id.followerId = :followerId")
    Page<CollectionFollowJpaEntity> findByFollowerId(@Param("followerId") UUID followerId, Pageable pageRequest);

    @Query("SELECT cf FROM CollectionFollowJpaEntity cf WHERE cf.id.collectionId = :collectionId")
    Page<CollectionFollowJpaEntity> findByCollectionId(@Param("collectionId") UUID collectionId, Pageable pageRequest);
}
