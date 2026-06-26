package com.collectto.api_collectto.infrastructure.persistence.tag;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.Tag;

@Component
public final class TagMapper {

    public TagJpaEntity toJpa(Tag tag) {
        TagJpaEntity entity = new TagJpaEntity();
        entity.setId(tag.getId());
        entity.setName(tag.getName());
        entity.setUsageCount(tag.getUsageCount());
        entity.setCreatedAt(tag.getCreatedAt());
        return entity;
    }

    public Tag toDomain(TagJpaEntity entity) {
        return new Tag(
            entity.getId(),
            entity.getName(),
            entity.getUsageCount(),
            entity.getCreatedAt()
        );
    }
}
