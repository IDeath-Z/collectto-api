package com.collectto.api_collectto.infrastructure.persistence.collectionfollow;

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
@Table(name = "collection_follows")
public class CollectionFollowJpaEntity {

    @EmbeddedId
    private CollectionFollowJpaId id;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;
}
