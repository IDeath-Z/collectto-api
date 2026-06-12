package com.collectto.api_collectto.application.usecases.itemcomment;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.ItemComment;
import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemCommentRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentItemUseCase {

    private final ItemCommentRepository itemCommentRepository;
    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;
    private final UserFollowRepository userFollowRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID itemId, UUID authorId, String content) {}
    public record Output(UUID commentId, UUID itemId, UUID authorId, String content, Instant createdAt) {}

    public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + input.itemId()));

        Collection collection = collectionRepository.findById(item.getCollectionId())
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + item.getCollectionId()));

        if (!collection.getUserId().equals(input.authorId())) {
            if (collection.getVisibility() == Visibility.PRIVATE)
                throw new ForbiddenActionException("User does not have permission to comment on this item");

            if (collection.getVisibility() == Visibility.FRIENDS)
                if (!userFollowRepository.isFollowing(input.authorId(), collection.getUserId()))
                    throw new ForbiddenActionException("User does not have permission to comment on this item");
        }

        ItemComment comment = ItemComment.createNewComment(
            input.itemId(),
            input.authorId(),
            input.content()
        );

        ItemComment savedComment = itemCommentRepository.save(comment);
        itemRepository.incrementCommentsCount(savedComment.getItemId());

        if (!savedComment.getAuthorId().equals(collection.getUserId())) {
            Notification notification = Notification.createItemCommentedNotification(
                collection.getUserId(),
                savedComment.getAuthorId(),
                savedComment.getItemId()
            );
            notificationRepository.save(notification);
        }

        return new Output(
            savedComment.getId(),
            savedComment.getItemId(),
            savedComment.getAuthorId(),
            savedComment.getContent(),
            savedComment.getCreatedAt()
        );
    }
}
