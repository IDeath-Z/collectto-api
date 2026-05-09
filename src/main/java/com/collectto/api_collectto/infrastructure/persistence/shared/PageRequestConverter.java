package com.collectto.api_collectto.infrastructure.persistence.shared;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.collectto.api_collectto.domain.shared.DomainPageRequest;

public final class PageRequestConverter {

    private PageRequestConverter() {
    }

    public static PageRequest toSpring(DomainPageRequest domainPage) {
        Sort sort = domainPage.sortBy().getDirection().equals("ASC")
        ? Sort.by(domainPage.sortBy().getField()).ascending()
        : Sort.by(domainPage.sortBy().getField()).descending();

        return PageRequest.of(domainPage.page(), domainPage.size(), sort);
    }
}
