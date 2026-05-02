package com.collectto.api_collectto.domain.shared;

import com.collectto.api_collectto.domain.enums.SortBy;

public record DomainPageRequest(
    int page, 
    int size,
    SortBy sortBy
) {}
