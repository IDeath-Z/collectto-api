package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface ItemRepository {
    
    DomainPageResult<Item> findByCollectionId(UUID collectionId, DomainPageRequest pageRequest);
    List<Item> findByUserId(UUID userId);
    Item save(Item item);
    Optional<Item> findById(UUID itemId);
    List<Item> findByCollectionIdAndName(UUID collectionId, String name);
    List<String> findTop3MediaUrlsByCollectionId(UUID collectionId);
    void incrementLikesCount(UUID itemId);
    void decrementLikesCount(UUID itemId);
    void incrementCommentsCount(UUID itemId);
    void decrementCommentsCount(UUID itemId);
}
