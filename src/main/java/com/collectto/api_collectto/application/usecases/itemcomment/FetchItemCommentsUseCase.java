package com.collectto.api_collectto.application.usecases.itemcomment;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.ItemComment;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemCommentRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchItemCommentsUseCase {

    private final ItemCommentRepository itemCommentRepository;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;

    public record Input(UUID itemId, UUID requesterId, DomainPageRequest pageRequest) {}
    public record CommenterSummary(UUID commentId, UUID userId, String username, String profilePictureURL, String content, Instant createdAt) {}
    public record Output(List<CommenterSummary> commenterSummaries, int totalPages, long totalElements, int currentPage) {}

        public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + input.itemId()));

        Collection collection = collectionRepository.findById(item.getCollectionId())
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + item.getCollectionId()));

        if (!collection.getUserId().equals(input.requesterId())) {
            if (collection.getVisibility() == Visibility.PRIVATE)
                throw new ForbiddenActionException("User does not have permission to view comments on this item");

            if (collection.getVisibility() == Visibility.FRIENDS)
                if (!userFollowRepository.isFollowing(input.requesterId(), collection.getUserId()))
                    throw new ForbiddenActionException("User does not have permission to view comments on this item");
        }

        DomainPageResult<ItemComment> commentPage = itemCommentRepository.findByItemId(input.itemId(), input.pageRequest());

        List<UUID> authorsIds = commentPage.content().stream()
            .map(ItemComment::getAuthorId)
            .toList();

        List<User> users = userRepository.findAllByIds(authorsIds);

        Map<UUID, User> userMap = users.stream()
            .collect(Collectors.toMap(User::getId, user -> user));

        List<CommenterSummary> commentators = commentPage.content().stream()
            .filter(comment -> userMap.containsKey(comment.getAuthorId()))
            .map(comment -> {
                User author = userMap.get(comment.getAuthorId());
                
                return new CommenterSummary(
                    comment.getId(),
                    author.getId(),
                    author.getUsername(),
                    author.getProfilePictureUrl(),
                    comment.getContent(),
                    comment.getCreatedAt()
                );
            })
            .toList();


        return new Output(
            commentators, 
            commentPage.totalPages(), 
            commentPage.totalElements(),
            commentPage.page()
        );
    }
}
