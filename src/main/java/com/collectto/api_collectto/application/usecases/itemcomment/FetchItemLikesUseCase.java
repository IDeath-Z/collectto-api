package com.collectto.api_collectto.application.usecases.itemcomment;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.ItemLike;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemLikeRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchItemLikesUseCase {

    private final ItemLikeRepository itemLikeRepository;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public record Input(UUID itemId, UUID requesterId, DomainPageRequest pageRequest) {}
    public record LikerSummary(UUID userId, String name, String username, String profilePictureURL) {}
    public record Output(List<LikerSummary> likers, int totalPages, long totalElements, int currentPage) {}

    public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new RuntimeException("Item not found"));

        Collection collection = collectionRepository.findById(item.getCollectionId())
            .orElseThrow(() -> new RuntimeException("Collection not found"));

        if (!collection.getUserId().equals(input.requesterId()) && collection.getVisibility() == Visibility.PRIVATE)
            throw new RuntimeException("Unauthorized access to private collection"); // Implement better exception handling as needed

        DomainPageResult<ItemLike> pageableItemLikes = itemLikeRepository.findByItemId(input.itemId(), input.pageRequest());

        List<UUID> likerIds = pageableItemLikes.content().stream()
            .map(ItemLike::getLikerId)
            .toList();

        List<User> users = userRepository.findAllByIds(likerIds);

        Map<UUID, User> userMap = users.stream()
            .collect(Collectors.toMap(User::getId, user -> user));

        // Just to guarantee the order of likers is the same as the order of item likes, we will use graphs later for recommendations
        List<LikerSummary> likers = pageableItemLikes.content().stream()
            .map(liker -> userMap.get(liker.getLikerId()))
            .filter(user -> user != null) // Technically should not happen, but just in case
            .map(user -> new LikerSummary(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getProfilePictureUrl()
            ))
            .toList();

        return new Output(
            likers, 
            pageableItemLikes.totalPages(), 
            pageableItemLikes.totalElements(), 
            pageableItemLikes.page()
        );
    }
}
