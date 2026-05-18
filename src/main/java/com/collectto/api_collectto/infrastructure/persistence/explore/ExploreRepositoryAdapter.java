package com.collectto.api_collectto.infrastructure.persistence.explore;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.collectto.api_collectto.domain.enums.SocialContext;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.ExploreRepository;
import com.collectto.api_collectto.domain.shared.DomainExploreCard;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.item.CollectionMediaProjection;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExploreRepositoryAdapter implements ExploreRepository {

    private final TagJpaRepository tagRepository;
    private final ItemJpaRepository itemRepository;
    private final CollectionJpaRepository collectionRepository;
    private final ExploreMapper exploreMapper;
    
    @Override
    public Set<UUID> getFavoriteTagIds(UUID userId) {
        return tagRepository.findFavoriteTagsByUserId(userId).stream()
            .map(TagJpaEntity::getId)
            .collect(Collectors.toSet());
    }

    @Override
    public List<DomainExploreCard> getItemsByUserTagsAffinity(Set<UUID> tags, DomainPageRequest pageRequest) {
        if (tags.isEmpty())
            return List.of();

        PageRequest springPage = PageConverter.toSpring(pageRequest);

        return exploreMapper.toItemDomain(
            itemRepository.findRecommendedByTags(tags, springPage),
            SocialContext.ITEM
        );
    }

    @Override
    public List<DomainExploreCard> getItemsByPopularity(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        return exploreMapper.toItemDomain(
            itemRepository.findTrendingItems(springPage),
            SocialContext.ITEM
        );
    }

    @Override
    public List<DomainExploreCard> getItemsByMostRecent(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        return exploreMapper.toItemDomain(
            itemRepository.findLatestItems(springPage),
            SocialContext.ITEM
        );
    }

    @Override
    public List<DomainExploreCard> getCollectionsByUserTagsAffinity(Set<UUID> tags, DomainPageRequest pageRequest) {
        if (tags.isEmpty()) 
            return List.of();

        PageRequest springPage = PageConverter.toSpring(pageRequest);

        List<CollectionJpaEntity> entities = collectionRepository.findRecommendedByTags(tags, Visibility.PUBLIC,springPage);
        Set<UUID> collectionIds = entities.stream().map(CollectionJpaEntity::getId).collect(Collectors.toSet());

        return exploreMapper.toCollectionDomain(entities, mapMedias(collectionIds), SocialContext.COLLECTION);
    }

    @Override
    public List<DomainExploreCard> getCollectionsByPopularity(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);

        List<CollectionJpaEntity> entities = collectionRepository.findTrendingCollections(Visibility.PUBLIC, springPage);

        if (entities.isEmpty())
            return List.of();

        Set<UUID> collectionIds = entities.stream().map(CollectionJpaEntity::getId).collect(Collectors.toSet());

        return exploreMapper.toCollectionDomain(entities, mapMedias(collectionIds), SocialContext.COLLECTION);
    }

    @Override
    public List<DomainExploreCard> getCollectionsByMostRecent(DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);

        List<CollectionJpaEntity> entities = collectionRepository.findLatestCollections(Visibility.PUBLIC, springPage);

        if (entities.isEmpty())
            return List.of();

        Set<UUID> collectionIds = entities.stream().map(CollectionJpaEntity::getId).collect(Collectors.toSet());

        return exploreMapper.toCollectionDomain(entities, mapMedias(collectionIds), SocialContext.COLLECTION);
    }

    private Map<UUID, List<String>> mapMedias(Set<UUID> collectionIds) {
        return collectionRepository.findMediaForCollections(collectionIds).stream()
            .collect(Collectors.toMap(
                CollectionMediaProjection::getCollectionId,
                proj -> proj.getMediaUrls().stream().limit(3).toList(),
                (v1, v2) -> v1
            ));
    }
}
