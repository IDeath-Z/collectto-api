package com.collectto.api_collectto.infrastructure.persistence.itemlike;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.ItemLike;

@Component
public class ItemLikeMapper {

    public ItemLikeJpaEntity toJpa(ItemLike itemLike) {
        ItemLikeJpaId id = new ItemLikeJpaId(itemLike.getItemId(), itemLike.getLikerId());

        ItemLikeJpaEntity entity = new ItemLikeJpaEntity();
        entity.setId(id);
        entity.setCreatedAt(itemLike.getCreatedAt());
        return entity;
    }

    public ItemLike toDomain(ItemLikeJpaEntity entity) {
        return new ItemLike(
                entity.getId().getItemId(),
                entity.getId().getLikerId(),
                entity.getCreatedAt()
        );
    }
}
