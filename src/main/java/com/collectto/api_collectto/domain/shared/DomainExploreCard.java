package com.collectto.api_collectto.domain.shared;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.SocialContext;

public record DomainExploreCard(
    UUID id,
    SocialContext context,
    List<String> imageUrls,
    List<String> tags
) {}
