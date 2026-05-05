package com.collectto.api_collectto.infrastructure.persistence.tag;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagResolverHelper {

    private final TagJpaRepository tagJpaRepository;

    public TagJpaEntity findOrCreate(String name) {
        return tagJpaRepository.findByName(name)
            .orElseGet(() -> {
                TagJpaEntity entity = new TagJpaEntity();
                entity.setId(UUID.randomUUID());
                entity.setName(name);
                return tagJpaRepository.save(entity);
            });
    }
}
