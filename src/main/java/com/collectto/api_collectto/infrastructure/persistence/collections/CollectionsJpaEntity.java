package com.collectto.api_collectto.infrastructure.persistence.collections;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "collections")
public class CollectionsJpaEntity {

    @Id
    @Column(name = "collection_id", nullable = false, unique = true)
    private UUID collectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_collections_user"))
    private UserJpaEntity user;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 20)
    private Visibility visibility;

    @Column(name = "followers_count", nullable = false, insertable = false)
    private int followersCount;

    @Column(name = "is_active", nullable = false, insertable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    @CreationTimestamp
    private Instant updatedAt;
}
