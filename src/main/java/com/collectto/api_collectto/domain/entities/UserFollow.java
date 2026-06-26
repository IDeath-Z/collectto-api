package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.FollowStatus;
import com.collectto.api_collectto.domain.shared.DomainValidator;

public final class UserFollow {

    private final UUID followerId;
    private final UUID followedId;
    private final FollowStatus status;
    private final Instant createdAt;

    public UserFollow(UUID followerId, UUID followedId, FollowStatus status, Instant createdAt) {
        if (followerId.equals(followedId))
            throw new IllegalArgumentException("A user cannot follow themselves");

        this.followerId = DomainValidator.requireNonNull(followerId, "Follower ID cannot be null");
        this.followedId = DomainValidator.requireNonNull(followedId, "Followed ID cannot be null");
        this.status = status;
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Creation date cannot be null");
    }

    public static UserFollow createNewFollow(UUID followerId, UUID followedId) {
        return new UserFollow(
            followerId,
            followedId,
            FollowStatus.PENDING,
            Instant.now()
        );
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public UUID getFollowedId() {
        return followedId;
    }

    public FollowStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UserFollow pending() {
        if (this.status != FollowStatus.DECLINED)
            throw new IllegalStateException("Only declined follow requests can be set to pending");
        return new UserFollow(this.followerId, this.followedId, FollowStatus.PENDING, this.createdAt);
    }

    public UserFollow accept() {
        if (this.status != FollowStatus.PENDING)
            throw new IllegalStateException("Only pending follow requests can be accepted");
        return new UserFollow(this.followerId, this.followedId, FollowStatus.ACCEPTED, this.createdAt);
    }

    public UserFollow decline() {
        if (this.status != FollowStatus.PENDING)
            throw new IllegalStateException("Only pending follow requests can be declined");
        return new UserFollow(this.followerId, this.followedId, FollowStatus.DECLINED, this.createdAt);
    } 
}
