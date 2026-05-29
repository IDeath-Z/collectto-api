package com.collectto.api_collectto.application.usecases.collectionfollow;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceAlreadyExistsException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.CollectionFollow;
import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionFollowRepository;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FollowCollectionUseCase {

    private final CollectionFollowRepository collectionFollowRepository;
    private final CollectionRepository collectionRepository;
    private final UserFollowRepository userFollowRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID followerId, UUID collectionId) {}
    public record Output(UUID followerId, UUID collectionId, String createdAt) {}

    public Output execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + input.collectionId()));

        if (collection.getUserId().equals(input.followerId()))
            throw new BusinessRuleException("User cannot follow their own collection");

        if (collection.getVisibility() == Visibility.PRIVATE)
             throw new ForbiddenActionException("User does not have permission to follow this collection");

        if (collection.getVisibility() == Visibility.FRIENDS)
                if (!userFollowRepository.isFollowing(input.followerId(), collection.getUserId()))
                    throw new ForbiddenActionException("User does not have permission to follow this collection");

        if (collectionFollowRepository.existsById(input.followerId(), input.collectionId()))
            throw new ResourceAlreadyExistsException("You are already following this collection.");

        CollectionFollow newFollow = CollectionFollow.createNewFollow(
            input.followerId(), 
            input.collectionId()
        );

        CollectionFollow savedFollow = collectionFollowRepository.save(newFollow);
        collectionRepository.incrementFollowers(savedFollow.getCollectionId());

        Notification notification = Notification.createCollectionFollowedNotification(
            collection.getUserId(),
            savedFollow.getFollowerId(),
            savedFollow.getCollectionId()
        );
        notificationRepository.save(notification);

        return new Output(
            savedFollow.getFollowerId(),
            savedFollow.getCollectionId(),
            savedFollow.getCreatedAt().toString()
        );
    }
}
