package com.collectto.api_collectto.infrastructure.persistence.userfollow;

import java.time.Instant;

import com.collectto.api_collectto.domain.enums.FollowStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_follows")
public class UserFollowJpaEntity {

    @EmbeddedId
    private UserFollowJpaId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private FollowStatus status;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;
}
