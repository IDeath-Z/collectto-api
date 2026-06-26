package com.collectto.api_collectto.application.usecases.userfollow;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.UserFollow;
import com.collectto.api_collectto.domain.enums.FollowStatus;
import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UnfollowUserUseCase {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID followerId, UUID followedId) {}

    public void execute(Input input) {
        if (input.followerId().equals(input.followedId()))
            throw new BusinessRuleException("Follower and followed users must be different");

        UserFollow existingFollow = userFollowRepository.findById(input.followerId(), input.followedId())
            .orElseThrow(() -> new ResourceNotFoundException("Following relationship not found with followerId: " + input.followerId() + " and followedId: " + input.followedId()));

        if (existingFollow.getStatus() == FollowStatus.DECLINED)
            throw new BusinessRuleException("Cannot unfollow a declined request");

        userFollowRepository.deleteById(existingFollow.getFollowerId(), existingFollow.getFollowedId());


        if (existingFollow.getStatus() == FollowStatus.PENDING) {
            notificationRepository.deleteByRecipientIdAndActorIdAndContext(
                existingFollow.getFollowedId(), 
                existingFollow.getFollowerId(), 
                NotificationContext.USER_FOLLOW_REQUESTED
            );
        }
        if (existingFollow.getStatus() == FollowStatus.ACCEPTED) {
            userRepository.decrementFollowers(existingFollow.getFollowedId());
            userRepository.decrementFollowing(existingFollow.getFollowerId());
            notificationRepository.deleteByRecipientIdAndActorIdAndContext(
                existingFollow.getFollowedId(), 
                existingFollow.getFollowerId(), 
                NotificationContext.USER_ACCEPTED_FOLLOW_REQUEST
            );
        }
    }
}
