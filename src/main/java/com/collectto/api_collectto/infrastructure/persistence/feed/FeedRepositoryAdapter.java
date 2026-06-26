package com.collectto.api_collectto.infrastructure.persistence.feed;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.collectto.api_collectto.domain.enums.FollowStatus;
import com.collectto.api_collectto.domain.enums.SocialContext;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.FeedRepository;
import com.collectto.api_collectto.domain.shared.DomainFeedCard;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.infrastructure.persistence.collectionfollow.CollectionFollowJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemMapper;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;
import com.collectto.api_collectto.infrastructure.persistence.userfollow.UserFollowJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class FeedRepositoryAdapter implements FeedRepository {

    private final UserFollowJpaRepository userFollowRepository;
    private final CollectionFollowJpaRepository collectionFollowRepository;
    private final ItemJpaRepository itemRepository;
    private final ItemMapper itemMapper;

@Override
    public List<DomainFeedCard> getFeed(UUID userId, DomainPageRequest pageRequest) {
        
        Set<UUID> followedUsers = userFollowRepository.findFollowedUserIds(userId, FollowStatus.ACCEPTED);
        Set<UUID> followedCollections = collectionFollowRepository.findFollowedCollectionIds(userId);
        Set<UUID> friendUsers = userFollowRepository.findMutualFriendIds(userId, FollowStatus.ACCEPTED);

        if (followedUsers.isEmpty() && followedCollections.isEmpty()) {
            return List.of();
        }

        PageRequest springPage = PageConverter.toSpring(pageRequest);
        
        List<ItemJpaEntity> itens = itemRepository.findFeedItems(
            followedUsers, 
            followedCollections, 
            friendUsers, 
            Visibility.PUBLIC, 
            Visibility.FRIENDS, 
            springPage
        );

        return itens.stream().map(item -> {
            DomainFeedCard.FeedSource source;
            if (followedUsers.contains(item.getUser().getId())) {
                source = new DomainFeedCard.FeedSource(
                    item.getUser().getId(), 
                    item.getUser().getUsername(), 
                    null,
                    item.getUser().getProfilePictureUrl(),
                    SocialContext.USER

                );
            } else {
                source = new DomainFeedCard.FeedSource(
                    item.getCollection().getId(),
                    null,
                    item.getCollection().getName(),
                    item.getCollection().getCoverImageUrl(),
                    SocialContext.COLLECTION
                );
            }
            return new DomainFeedCard(source, itemMapper.toDomain(item));
        }).toList();
    }
}
