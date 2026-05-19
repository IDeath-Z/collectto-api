package com.collectto.api_collectto.infrastructure.persistence.user;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<UserJpaEntity> findByEmail(String email);

    @Query("SELECT u FROM UserJpaEntity u WHERE u.isActive = true AND u.username ILIKE %:query%")
    Page<UserJpaEntity> searchActiveUsers(@Param("query") String query, Pageable pageable);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followersCount = u.followersCount + 1 WHERE u.id = :userId")
    void incrementFollowers(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followingCount = u.followingCount + 1 WHERE u.id = :userId")
    void incrementFollowing(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followersCount = u.followersCount - 1 WHERE u.id = :userId AND u.followersCount > 0")
    void decrementFollowers(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.followingCount = u.followingCount - 1 WHERE u.id = :userId AND u.followingCount > 0")
    void decrementFollowing(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.isActive = false, u.deactivatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    void deactivateUser(@Param("userId") UUID userId);

    @Modifying @Query("UPDATE UserJpaEntity u SET u.isActive = true, u.deactivatedAt = null WHERE u.id = :userId")
    void reactivateAccount(@Param("userId") UUID userId);

    @Modifying @Query(value = """
        DELETE FROM users 
        WHERE is_active = false 
        AND deactivated_at <= :cutoffDate
    """, nativeQuery = true)
    int purgeOldDeactivatedUsers(@Param("cutoffDate") Instant cutoffDate);

    @Query(value = """
        SELECT * FROM users 
        WHERE is_active = false 
        AND deactivated_at <= :cutoffDate
    """, nativeQuery = true)
    List<UserJpaEntity> findUsersToPurge(@Param("cutoffDate") Instant cutoffDate);
}
