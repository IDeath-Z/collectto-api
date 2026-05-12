package com.collectto.api_collectto.infrastructure.persistence.itemlike;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "item_likes")
public class ItemLikeJpaEntity {

    @EmbeddedId
    private ItemLikeJpaId id;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;
}
