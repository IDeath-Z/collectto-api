package com.collectto.api_collectto.infrastructure.persistence.item;

import java.util.List;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;

@Component
public class ItemMapper {

    public ItemJpaEntity toJpa(Item item, CollectionJpaEntity collection, UserJpaEntity user) {
        ItemJpaEntity entity = new ItemJpaEntity();
        entity.setId(item.getId());
        entity.setCollection(collection);
        entity.setUser(user);
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        entity.setAcquisitionDate(item.getAcquisitionDate());
        entity.setLastUsedDate(item.getLastUsedDate());
        entity.setMediaUrls(item.getMediaURLs());
        entity.setAttributes(item.getAttributes());
        entity.setLikesCount(item.getLikesCount());
        entity.setCommentsCount(item.getCommentsCount());
        entity.setActive(item.isActive());
        entity.setCreatedAt(item.getCreatedAt());
        entity.setUpdatedAt(item.getUpdatedAt());
        return entity;
    }

    public Item toDomain(ItemJpaEntity entity) {
        List<String> tags = entity.getTags().stream()
            .map(TagJpaEntity::getName)
            .toList();

        return new Item(
            entity.getId(),
            entity.getCollection().getId(),
            entity.getUser().getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getAcquisitionDate(),
            entity.getLastUsedDate(),
            entity.getMediaUrls(),
            entity.getAttributes(),
            entity.getLikesCount(),
            entity.getCommentsCount(),
            tags,
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
