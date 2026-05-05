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

    Optional<TagJpaEntity> findByName(String name);
}
