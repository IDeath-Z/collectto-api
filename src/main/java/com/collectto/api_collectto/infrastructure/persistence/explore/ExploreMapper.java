package com.collectto.api_collectto.infrastructure.persistence.explore;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.enums.SocialContext;
import com.collectto.api_collectto.domain.shared.DomainExploreCard;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagJpaEntity;

@Component
public class ExploreMapper {

    public List<DomainExploreCard> toItemDomain(List<ItemJpaEntity> entities, SocialContext context) {
        return entities.stream()
            .map(entity -> new DomainExploreCard(
                entity.getId(),
                context,
                entity.getMediaUrls() != null ? entity.getMediaUrls().stream().limit(3).toList() : List.of(),
                entity.getTags().stream().map(TagJpaEntity::getName).toList()
            ))
            .toList();
    }

    public List<DomainExploreCard> toCollectionDomain(List<CollectionJpaEntity> entities, Map<UUID, List<String>> medias, SocialContext context) {
        return entities.stream().map(entity -> new DomainExploreCard(
            entity.getId(),
            context,
            medias.getOrDefault(entity.getId(), List.of()),
            entity.getTags().stream().map(TagJpaEntity::getName).toList()
        )).toList();
    }
}
