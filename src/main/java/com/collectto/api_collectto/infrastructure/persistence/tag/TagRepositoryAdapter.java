package com.collectto.api_collectto.infrastructure.persistence.tag;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.Tag;
import com.collectto.api_collectto.domain.ports.TagRepository;

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
}
