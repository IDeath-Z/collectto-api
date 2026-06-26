package com.collectto.api_collectto.infrastructure.persistence.itemcomment;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.ItemComment;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;

@Component
public final class ItemCommentMapper {

    public ItemCommentJpaEntity toJpa(ItemComment itemComment, ItemJpaEntity item, UserJpaEntity author) {
        ItemCommentJpaEntity entity = new ItemCommentJpaEntity();
        entity.setId(itemComment.getId());
        entity.setItem(item);
        entity.setAuthor(author);
        entity.setContent(itemComment.getContent());
        entity.setCreatedAt(itemComment.getCreatedAt());
        return entity;
    }

    public ItemComment toDomain(ItemCommentJpaEntity entity) {
        return new ItemComment(
            entity.getId(),
            entity.getItem().getId(),
            entity.getAuthor().getId(),
            entity.getContent(),
            entity.getCreatedAt()
        );
    }
}
