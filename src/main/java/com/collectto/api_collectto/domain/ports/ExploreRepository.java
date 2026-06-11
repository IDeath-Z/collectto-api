package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainExploreCard;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;

public interface ExploreRepository {

    Set<UUID> getFavoriteTagIds(UUID userId);
    
    List<DomainExploreCard> getItemsByUserTagsAffinity(UUID requesterId, Set<UUID> tags, Set<UUID> excludedCollectionIds, DomainPageRequest pageRequest);
    List<DomainExploreCard> getItemsByPopularity(UUID requesterId, Set<UUID> excludedCollectionIds, DomainPageRequest pageRequest);
    List<DomainExploreCard> getItemsByMostRecent(UUID requesterId, Set<UUID> excludedCollectionIds, DomainPageRequest pageRequest);
    
    List<DomainExploreCard> getCollectionsByUserTagsAffinity(UUID requesterId, Set<UUID> tags, DomainPageRequest pageRequest);
    List<DomainExploreCard> getCollectionsByPopularity(UUID requesterId, DomainPageRequest pageRequest);
    List<DomainExploreCard> getCollectionsByMostRecent(UUID requesterId, DomainPageRequest pageRequest);
}
