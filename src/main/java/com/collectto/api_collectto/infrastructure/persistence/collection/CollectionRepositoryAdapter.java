package com.collectto.api_collectto.infrastructure.persistence.collection;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
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
    public List<Collection> findByUserId(UUID userId) {
        return collectionsJpaRepository.findByUserId(userId).stream()
            .map(collectionMapper::toDomain)
            .toList();
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
    public List<Collection> findByUserIdAndName(UUID userId, String name) {
        return collectionsJpaRepository.findByUserIdAndNameContainingIgnoreCase(userId, name).stream()
            .map(collectionMapper::toDomain)
            .toList();
    }
}
