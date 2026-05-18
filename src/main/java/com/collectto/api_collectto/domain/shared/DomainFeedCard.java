package com.collectto.api_collectto.domain.shared;

import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.enums.SocialContext;

public record DomainFeedCard(
    FeedSource source,
    Item item
) {
    public record FeedSource(
        UUID id,
        String username,      
        String collectionName,
        String avatarUrl,
        SocialContext context
    ) {}
}