package com.collectto.api_collectto.infrastructure.persistence.tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, UUID> {

    @Query(value = """
        SELECT * FROM tags
        WHERE name ILIKE :prefix || '%'
        ORDER BY name
        LIMIT :limit
        """, nativeQuery = true
    )
    List<TagJpaEntity> findSuggestions(@Param("prefix") String prefix, @Param("limit") int limit);

    @Query(value = """
        SELECT DISTINCT t.* FROM tags t
        JOIN collection_tags ct ON t.tag_id = ct.tag_id
        JOIN collection_follows ufc ON ct.collection_id = ufc.collection_id
        WHERE ufc.follower_id = :userId
        LIMIT 20
        """, nativeQuery = true)
    List<TagJpaEntity> findFavoriteTagsByUserId(@Param("userId") UUID userId);

    Optional<TagJpaEntity> findByName(String name);
}
