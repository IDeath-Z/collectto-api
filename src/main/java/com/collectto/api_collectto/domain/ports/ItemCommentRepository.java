package com.collectto.api_collectto.domain.ports;

import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.ItemComment;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface ItemCommentRepository {

    Optional<ItemComment> findById(UUID id);
    ItemComment save(ItemComment comment);
    DomainPageResult<ItemComment> findByItemId(UUID itemId, DomainPageRequest pageRequest);
    DomainPageResult<ItemComment> findByAuthorId(UUID authorId, DomainPageRequest pageRequest);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
