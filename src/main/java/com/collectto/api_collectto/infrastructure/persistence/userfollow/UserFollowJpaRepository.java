package com.collectto.api_collectto.infrastructure.persistence.userfollow;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectto.api_collectto.domain.enums.FollowStatus;


public interface UserFollowJpaRepository extends JpaRepository<UserFollowJpaEntity, UserFollowJpaId> {

    @Query("SELECT u FROM UserFollowJpaEntity u WHERE u.id.followerId = :followerId AND u.status = :status")
    Page<UserFollowJpaEntity> findByFollowerIdAndStatus(@Param("followerId") UUID followerId, @Param("status") FollowStatus status, Pageable pageRequest);
    
    @Query("SELECT u FROM UserFollowJpaEntity u WHERE u.id.followedId = :followedId AND u.status = :status")
    Page<UserFollowJpaEntity> findByFollowedIdAndStatus(@Param("followedId") UUID followedId, @Param("status") FollowStatus status, Pageable pageRequest);
}
