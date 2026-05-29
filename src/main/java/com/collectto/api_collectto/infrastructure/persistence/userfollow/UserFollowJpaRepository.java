package com.collectto.api_collectto.infrastructure.persistence.userfollow;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectto.api_collectto.domain.enums.FollowStatus;


public interface UserFollowJpaRepository extends JpaRepository<UserFollowJpaEntity, UserFollowJpaId> {

    @Query("SELECT if FROM UserFollowJpaEntity if WHERE if.id.followerId = :followerId AND if.status = :status")
    Page<UserFollowJpaEntity> findByFollowerIdAndStatus(@Param("followerId") UUID followerId, @Param("status") FollowStatus status, Pageable pageRequest);
    
    @Query("SELECT if FROM UserFollowJpaEntity if WHERE if.id.followedId = :followedId AND if.status = :status")
    Page<UserFollowJpaEntity> findByFollowedIdAndStatus(@Param("followedId") UUID followedId, @Param("status") FollowStatus status, Pageable pageRequest);

    @Query("""
        SELECT uf.id.followedId FROM UserFollowJpaEntity uf 
        WHERE uf.id.followerId = :userId 
        AND uf.status = :status
    """)
    Set<UUID> findFollowedUserIds(@Param("userId") UUID userId, @Param("status") FollowStatus status);

    @Query("""
        SELECT uf1.id.followedId FROM UserFollowJpaEntity uf1 
        INNER JOIN UserFollowJpaEntity uf2 
            ON uf1.id.followedId = uf2.id.followerId 
        WHERE uf1.id.followerId = :userId 
        AND uf2.id.followedId = :userId 
        AND uf1.status = :status 
        AND uf2.status = :status
    """)
    Set<UUID> findMutualFriendIds(@Param("userId") UUID userId, @Param("status") FollowStatus status);

    boolean existsByIdFollowerIdAndIdFollowedIdAndStatus(UUID followerId, UUID followedId, FollowStatus status);
}
