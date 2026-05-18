package com.collectto.api_collectto.application.usecases.explore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.SortBy;
import com.collectto.api_collectto.domain.ports.ExploreRepository;
import com.collectto.api_collectto.domain.shared.DomainExploreCard;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchExploreUseCase {

    private final ExploreRepository exploreRepository;

    public record Input(UUID userId, int page, int size) {}
    public record Output(List<DomainExploreCard> content, int page, int size, boolean hasNext) {}

    public Output execute(Input input) {
        DomainPageRequest pageRequest = new DomainPageRequest(input.page(), input.size() / 2, SortBy.CREATED_AT_DESC);
        List<DomainExploreCard> exploreFeed = new ArrayList<>();
        
        Set<UUID> favoriteTagIds = exploreRepository.getFavoriteTagIds(input.userId());

        List<DomainExploreCard> items = new ArrayList<>(exploreRepository.getItemsByUserTagsAffinity(favoriteTagIds, pageRequest));
        
        int missingItems = pageRequest.size() - items.size();
        if (missingItems > 0) {
            items.addAll(exploreRepository.getItemsByPopularity(new DomainPageRequest(pageRequest.page(), missingItems, pageRequest.sortBy())));
            
            int stillMissing = pageRequest.size() - items.size();
            if (stillMissing > 0) {
                items.addAll(exploreRepository.getItemsByMostRecent(new DomainPageRequest(pageRequest.page(), stillMissing, pageRequest.sortBy())));
            }
        }
        exploreFeed.addAll(items);

        List<DomainExploreCard> collections = new ArrayList<>(exploreRepository.getCollectionsByUserTagsAffinity(favoriteTagIds, pageRequest));
        
        int missingCollections = pageRequest.size() - collections.size();
        if (missingCollections > 0) {
            collections.addAll(exploreRepository.getCollectionsByPopularity(new DomainPageRequest(pageRequest.page(), missingCollections, pageRequest.sortBy())));
            
            int stillMissing = pageRequest.size() - collections.size();
            if (stillMissing > 0) {
                collections.addAll(exploreRepository.getCollectionsByMostRecent(new DomainPageRequest(pageRequest.page(), stillMissing, pageRequest.sortBy())));
            }
        }
        exploreFeed.addAll(collections);

        Collections.shuffle(exploreFeed);

        boolean hasNext = exploreFeed.size() == input.size();
        
        return new Output(
            exploreFeed, 
            input.page(), 
            input.size(), 
            hasNext
        );
    }
}