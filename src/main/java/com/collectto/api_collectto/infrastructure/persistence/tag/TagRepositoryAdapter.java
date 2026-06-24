package com.collectto.api_collectto.infrastructure.persistence.tag;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.Tag;
import com.collectto.api_collectto.domain.ports.TagRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TagRepositoryAdapter implements TagRepository {

    private final TagJpaRepository jpaRepository;
    private final TagMapper tagmapper;

    @Override
    public List<Tag> findSuggestions(String prefix, int limit) {
        return jpaRepository.findSuggestions(prefix, limit)
            .stream()
            .map(tagmapper::toDomain)
            .toList();
    }

    @Override
    public DomainPageResult<Tag> findByName(String name, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<TagJpaEntity> page = jpaRepository.findByNameContainingIgnoreCaseOrderByUsageCountDesc(name, springPage);

        return PageConverter.toDomain(page, tagmapper::toDomain);
    }
}
