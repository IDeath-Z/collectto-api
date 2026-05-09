package com.collectto.api_collectto.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<UserJpaEntity> findByEmail(String email);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followersCount = u.followersCount + 1 WHERE u.id = :userId")
    void incrementFollowers(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followingCount = u.followingCount + 1 WHERE u.id = :userId")
    void incrementFollowing(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followersCount = u.followersCount - 1 WHERE u.id = :userId AND u.followersCount > 0")
    void decrementFollowers(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followingCount = u.followingCount - 1 WHERE u.id = :userId AND u.followingCount > 0")
    void decrementFollowing(@Param("userId") UUID userId);
}
