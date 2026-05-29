package com.collectto.api_collectto.domain.ports;

import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.UserFollow;
import com.collectto.api_collectto.domain.enums.FollowStatus;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface UserFollowRepository {

    Optional<UserFollow> findById(UUID followerId, UUID followedId);
    UserFollow save(UserFollow userFollow);
    DomainPageResult<UserFollow> findByFollowerIdAndStatus(UUID followerId, FollowStatus status, DomainPageRequest pageRequest);
    DomainPageResult<UserFollow> findByFollowedIdAndStatus(UUID followedId, FollowStatus status, DomainPageRequest pageRequest);
    boolean isFollowing(UUID followerId, UUID followedId);
    void deleteById(UUID followerId, UUID followedId);
}
