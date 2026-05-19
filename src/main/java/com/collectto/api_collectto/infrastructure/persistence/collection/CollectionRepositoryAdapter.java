package com.collectto.api_collectto.infrastructure.persistence.collection;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.tag.TagResolverHelper;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CollectionRepositoryAdapter implements CollectionRepository {

    private final CollectionJpaRepository collectionsJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CollectionMapper collectionMapper;
    private final TagResolverHelper tagResolverHelper;

    @Override
    public DomainPageResult<Collection> findByUserId(UUID userId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<CollectionJpaEntity> page = collectionsJpaRepository.findByUserId(userId, springPage);
    
        return PageConverter.toDomain(page, collectionMapper::toDomain);
    }

    @Override
    public Collection save(Collection collection) {
        UserJpaEntity user = userJpaRepository.findById(collection.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        CollectionJpaEntity entity = collectionMapper.toJpa(collection, user);

        Set<TagJpaEntity> tagEntities = collection.getTags().stream()
            .map(tagResolverHelper::findOrCreate)
            .collect(Collectors.toSet());

        entity.setTags(tagEntities);

        return collectionMapper.toDomain(collectionsJpaRepository.save(entity));
    }

    @Override
    public Optional<Collection> findById(UUID id) {
        return collectionsJpaRepository.findById(id).map(collectionMapper::toDomain);
    }

    @Override
    public List<Collection> findAllByIds(List<UUID> ids) {
        return collectionsJpaRepository.findAllById(ids)
            .stream()
            .map(collectionMapper::toDomain)
            .toList();
    }

    @Override
    public List<Collection> findByUserIdAndName(UUID userId, String name) {
        return collectionsJpaRepository.findByUserIdAndNameContainingIgnoreCase(userId, name).stream()
            .map(collectionMapper::toDomain)
            .toList();
    }

    @Override
    public DomainPageResult<Collection> searchPublicCollections(String query, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<CollectionJpaEntity> page = collectionsJpaRepository.searchPublicCollections(query, Visibility.PUBLIC, springPage);

        return PageConverter.toDomain(page, collectionMapper::toDomain);
    }

    @Override
    public DomainPageResult<Collection> findVisibleCollections(UUID userId, UUID requesterId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<CollectionJpaEntity> page = collectionsJpaRepository.findVisibleCollections(userId, requesterId, Visibility.PRIVATE, springPage);

        return PageConverter.toDomain(page, collectionMapper::toDomain);
    }

    @Override
    public void incrementFollowers(UUID collectionId) {
        collectionsJpaRepository.incrementFollowers(collectionId);
    }

    @Override
    public void decrementFollowers(UUID collectionId) {
        collectionsJpaRepository.decrementFollowers(collectionId);
    }

    @Override
    public void deactivateCollection(UUID collectionId) {
        collectionsJpaRepository.deactivateCollection(collectionId);
    }
}
