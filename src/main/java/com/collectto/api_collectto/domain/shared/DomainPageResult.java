package com.collectto.api_collectto.domain.shared;

import java.util.List;

public record DomainPageResult<T>(
    List<T> content, 
    int page, 
    int size, 
    long totalElements, 
    int totalPages
) {}
