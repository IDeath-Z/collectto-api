package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainExploreCard;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;

public interface ExploreRepository {

    Set<UUID> getFavoriteTagIds(UUID userId);
    
    List<DomainExploreCard> getItemsByUserTagsAffinity(Set<UUID> tags, DomainPageRequest pageRequest);
    List<DomainExploreCard> getItemsByPopularity(DomainPageRequest pageRequest);
    List<DomainExploreCard> getItemsByMostRecent(DomainPageRequest pageRequest);
    
    List<DomainExploreCard> getCollectionsByUserTagsAffinity(Set<UUID> tags, DomainPageRequest pageRequest);
    List<DomainExploreCard> getCollectionsByPopularity(DomainPageRequest pageRequest);
    List<DomainExploreCard> getCollectionsByMostRecent(DomainPageRequest pageRequest);
}
