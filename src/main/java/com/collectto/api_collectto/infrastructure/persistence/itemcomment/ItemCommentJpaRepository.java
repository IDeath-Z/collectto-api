package com.collectto.api_collectto.infrastructure.persistence.itemcomment;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemCommentJpaRepository extends JpaRepository<ItemCommentJpaEntity, UUID> {

    @Query("SELECT ic FROM ItemCommentJpaEntity ic WHERE ic.item.id = :itemId")
    Page<ItemCommentJpaEntity> findByItemId(@Param("itemId")UUID itemId, Pageable pageable);

    @Query("SELECT ic FROM ItemCommentJpaEntity ic WHERE ic.author.id = :authorId")
    Page<ItemCommentJpaEntity> findByAuthorId(@Param("authorId")UUID authorId, Pageable pageable);
}
