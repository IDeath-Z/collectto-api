package com.collectto.api_collectto.application.usecases.userfollow;

import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.entities.UserFollow;
import com.collectto.api_collectto.domain.enums.FollowStatus;
import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AcceptFollowUseCase {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID followerId, UUID followedId) {}
    public record Output(UUID followerId, UUID followedId, FollowStatus status, String createdAt) {}

    public Output execute(Input input) {
        if (input.followerId().equals(input.followedId()))
            throw new IllegalArgumentException("Follower and followed users must be different.");

        UserFollow followRequest = userFollowRepository.findById(input.followerId(), input.followedId())
            .orElseThrow(() -> new IllegalStateException("Follow request not found."));

        if (followRequest.getStatus() == FollowStatus.ACCEPTED)
            throw new IllegalStateException("Already following.");
        if (followRequest.getStatus() == FollowStatus.DECLINED)
            throw new IllegalStateException("Cannot accept a declined follow request.");

        UserFollow acceptedFollow = userFollowRepository.save(followRequest.accept());
        userRepository.incrementFollowers(acceptedFollow.getFollowedId());

        Optional<Notification> notificationOpt = notificationRepository.findByRecipientIdAndActorIdAndContext(
            input.followedId(), 
            input.followerId(), 
            NotificationContext.USER_FOLLOW_REQUESTED
        );

        if (notificationOpt.isPresent()) {
            Notification updatedNotification = notificationOpt.get().updateContext(NotificationContext.USER_ACCEPTED_FOLLOW_REQUEST);
            notificationRepository.save(updatedNotification);
        }

        return new Output(
            acceptedFollow.getFollowerId(), 
            acceptedFollow.getFollowedId(), 
            acceptedFollow.getStatus(), 
            acceptedFollow.getCreatedAt().toString()
        );
    }
}
