package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Item;

public interface ItemRepository {
    List<Item> findByCollectionId(UUID collectionId);
    List<Item> findByUserId(UUID userId);
    Item save(Item item);
    Optional<Item> findById(UUID itemId);
    List<Item> findByCollectionIdAndName(UUID collectionId, String name);
}
