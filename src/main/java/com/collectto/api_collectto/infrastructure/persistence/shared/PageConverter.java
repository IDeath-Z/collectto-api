package com.collectto.api_collectto.infrastructure.persistence.shared;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public final class PageConverter {

    private PageConverter() {}

    public static PageRequest toSpring(DomainPageRequest domainPage) {
        Sort sort = domainPage.sortBy().getDirection().equals("ASC")
        ? Sort.by(domainPage.sortBy().getField()).ascending()
        : Sort.by(domainPage.sortBy().getField()).descending();

        return PageRequest.of(domainPage.page(), domainPage.size(), sort);
    }

    public static <DomainEntity, JpaEntity> DomainPageResult<DomainEntity> toDomain(Page<JpaEntity> page, Function<JpaEntity, DomainEntity> mapper) {
        return new DomainPageResult<>(
            page.getContent().stream().map(mapper).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
