package com.collectto.api_collectto.application.usecases.feed;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.SortBy;
import com.collectto.api_collectto.domain.ports.FeedRepository;
import com.collectto.api_collectto.domain.shared.DomainFeedCard;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchFeedUseCase {

    private final FeedRepository feedRepository;

    public record Input(UUID userId, int page, int size) {}
    public record Output(List<DomainFeedCard> content, int page, int size, boolean hasNext) {}

    public Output execute(Input input) {
        DomainPageRequest pageRequest = new DomainPageRequest(input.page(), input.size(), SortBy.CREATED_AT_DESC);
        
        List<DomainFeedCard> feed = feedRepository.getFeed(input.userId(), pageRequest);

        boolean hasNext = feed.size() == input.size();

        return new Output(feed, input.page(), input.size(), hasNext);
    }
}
