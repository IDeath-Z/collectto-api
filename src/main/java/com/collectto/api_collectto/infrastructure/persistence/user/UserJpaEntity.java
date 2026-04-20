package com.collectto.api_collectto.infrastructure.persistence.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "profile_picture_url", columnDefinition = "TEXT")
    private String profilePictureUrl;

    @Column(name = "followers_count", nullable = false, insertable = false)
    private int followersCount;

    @Column(name = "following_count", nullable = false, insertable = false)
    private int followingCount;

    @Column(name = "is_active", nullable = false, insertable = false)
    private boolean isActive;

    @Column(name = "birthday_date", nullable = false)
    private LocalDate birthdayDate;

    @Column(name = "creation_date", nullable = false, insertable = false, updatable = false)
    @CreationTimestamp
    private Instant creationDate;
}
