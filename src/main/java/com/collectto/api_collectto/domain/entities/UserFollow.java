package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.FollowStatus;

public class UserFollow {

    UUID followerId;
    UUID followedId;
    FollowStatus status;
    Instant createdAt;

    public UserFollow(UUID followerId, UUID followedId, FollowStatus status, Instant createdAt) {
        if (followerId == null || followedId == null || createdAt == null)
            throw new IllegalArgumentException("Ids and createdAt cannot be null");
        if (followerId.equals(followedId))
            throw new IllegalArgumentException("A user cannot follow themselves");

        this.followerId = followerId;
        this.followedId = followedId;
        this.status = status;
        this.createdAt = createdAt;
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
