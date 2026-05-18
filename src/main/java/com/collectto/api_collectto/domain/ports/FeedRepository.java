package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainFeedCard;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;

public interface FeedRepository {

    List<DomainFeedCard> getFeed(UUID userId, DomainPageRequest pageRequest);
}