package com.collectto.api_collectto.application.usecases.userfollow;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.entities.UserFollow;
import com.collectto.api_collectto.domain.enums.FollowStatus;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FollowUserUseCase {

    private final UserFollowRepository userFollowRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID followerId, UUID followedId) {}
    public record Output(UUID followerId, UUID followedId, FollowStatus status, Instant createdAt) {}

    public Output execute(Input input) {
        if (input.followerId().equals(input.followedId()))
            throw new BusinessRuleException("Follower and followed users must be different.");

        Optional<UserFollow> existingFollowOpt = userFollowRepository.findById(input.followerId(), input.followedId());

        UserFollow followToSave;

        if (existingFollowOpt.isPresent()) {
            UserFollow existingFollow = existingFollowOpt.get();

            if (existingFollow.getStatus() == FollowStatus.PENDING)
                throw new BusinessRuleException("Request already sent.");
            if (existingFollow.getStatus() == FollowStatus.ACCEPTED)
                throw new BusinessRuleException("Already following.");
            
            followToSave = existingFollow.pending(); // Declined follow can be re-requested
        } else {
            followToSave = UserFollow.createNewFollow(
                input.followerId(),
                input.followedId()
            );
        }

        UserFollow savedNewFollow = userFollowRepository.save(followToSave);

        Notification followRequestNotification = Notification.createUserFollowRequestNotification(
            savedNewFollow.getFollowedId(),
            savedNewFollow.getFollowerId()
        );
        notificationRepository.save(followRequestNotification);

        return new Output(
            savedNewFollow.getFollowerId(),
            savedNewFollow.getFollowedId(),
            savedNewFollow.getStatus(),
            savedNewFollow.getCreatedAt()
        );
    }
}

        

