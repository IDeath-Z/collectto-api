package com.collectto.api_collectto.infrastructure.persistence.item;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagResolverHelper;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepository {

    private final ItemJpaRepository itemJpaRepository;
    private final CollectionJpaRepository collectionsJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ItemMapper itemMapper;
    private final TagResolverHelper tagResolverHelper;

    @Override
    public DomainPageResult<Item> findByCollectionId(UUID collectionId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<ItemJpaEntity> page = itemJpaRepository.findByCollectionId(collectionId, springPage);

        return PageConverter.toDomain(page, itemMapper::toDomain);
    }

    @Override
    public List<Item> findByUserId(UUID userId) {
        return itemJpaRepository.findByUserId(userId).stream()
            .map(itemMapper::toDomain)
            .toList();
    }

    @Override
    public Item save(Item item) {
        CollectionJpaEntity collection = collectionsJpaRepository.findById(item.getCollectionId())
            .orElseThrow(() -> new RuntimeException("Collection not found"));

        UserJpaEntity user = userJpaRepository.findById(item.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        ItemJpaEntity entity = itemMapper.toJpa(item, collection, user);

        Set<TagJpaEntity> tagEntities = item.getTags().stream()
            .map(tagResolverHelper::findOrCreate)
            .collect(Collectors.toSet());

        entity.setTags(tagEntities);
        return itemMapper.toDomain(itemJpaRepository.save(entity));
    }

    @Override
    public Optional<Item> findById(UUID itemId) {
        return itemJpaRepository.findById(itemId).map(itemMapper::toDomain);
    }

    @Override
    public List<Item> findByCollectionIdAndName(UUID collectionId, String name) {
        return itemJpaRepository.findByCollectionIdAndNameContainingIgnoreCase(collectionId, name).stream()
            .map(itemMapper::toDomain)
            .toList();
    }

    @Override
    public List<String> findTop3MediaUrlsByCollectionId(UUID collectionId) {
        return itemJpaRepository.findTop3MediaUrlsByCollectionId(collectionId);
    }

    @Override
    public void incrementLikesCount(UUID itemId) {
        itemJpaRepository.incrementLikesCount(itemId);
    }

    @Override
    public void decrementLikesCount(UUID itemId) {
        itemJpaRepository.decrementLikesCount(itemId);
    }

    @Override
    public void incrementCommentsCount(UUID itemId) {
        itemJpaRepository.incrementCommentsCount(itemId);
    }

    @Override
    public void decrementCommentsCount(UUID itemId) {
        itemJpaRepository.decrementCommentsCount(itemId);
    }
}
