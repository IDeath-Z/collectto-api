package com.collectto.api_collectto.infrastructure.persistence.itemcomment;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.ItemComment;
import com.collectto.api_collectto.domain.ports.ItemCommentRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemCommentRepositoryAdapter implements ItemCommentRepository {

    private final ItemCommentJpaRepository itemCommentJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ItemCommentMapper itemCommentMapper;
    
    @Override
    public Optional<ItemComment> findById(UUID id) {
        return itemCommentJpaRepository.findById(id).map(itemCommentMapper::toDomain);
    }

    @Override
    public ItemComment save(ItemComment comment) {
        ItemJpaEntity item = itemJpaRepository.findById(comment.getItemId())
            .orElseThrow(() -> new IllegalArgumentException("Item not found with ID: " + comment.getItemId()));

        UserJpaEntity author = userJpaRepository.findById(comment.getAuthorId())
            .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + comment.getAuthorId()));

        ItemCommentJpaEntity entity = itemCommentMapper.toJpa(comment, item, author);
        return itemCommentMapper.toDomain(itemCommentJpaRepository.save(entity));
    }

    @Override
    public DomainPageResult<ItemComment> findByItemId(UUID itemId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<ItemCommentJpaEntity> page = itemCommentJpaRepository.findByItemId(itemId, springPage);

        return PageConverter.toDomain(page, itemCommentMapper::toDomain);
    }

    @Override
    public DomainPageResult<ItemComment> findByAuthorId(UUID authorId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<ItemCommentJpaEntity> page = itemCommentJpaRepository.findByAuthorId(authorId, springPage);

        return PageConverter.toDomain(page, itemCommentMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return itemCommentJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        itemCommentJpaRepository.deleteById(id);
    }
}
