package com.collectto.api_collectto.infrastructure.persistence.collectionfollow;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.CollectionFollow;
import com.collectto.api_collectto.domain.ports.CollectionFollowRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CollectionFollowRepositoryAdapter implements CollectionFollowRepository {

    private final CollectionFollowJpaRepository collectionFollowJpaRepository;
    private final CollectionFollowJpaMapper collectionFollowJpaMapper;

    @Override
    public Optional<CollectionFollow> findById(UUID followerId, UUID collectionId) {
        return collectionFollowJpaRepository.findById(new CollectionFollowJpaId(followerId, collectionId)).map(collectionFollowJpaMapper::toDomain);
    }

    @Override
    public CollectionFollow save(CollectionFollow collectionFollow) {
        CollectionFollowJpaEntity entity = collectionFollowJpaMapper.toJpa(collectionFollow);
        return collectionFollowJpaMapper.toDomain(collectionFollowJpaRepository.save(entity));
    }

    @Override
    public DomainPageResult<CollectionFollow> findByFollowerId(UUID followerId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<CollectionFollowJpaEntity> page = collectionFollowJpaRepository.findByFollowerId(followerId, springPage);

        return PageConverter.toDomain(page, collectionFollowJpaMapper::toDomain);
    }

    @Override
    public DomainPageResult<CollectionFollow> findByCollectionId(UUID collectionId, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<CollectionFollowJpaEntity> page = collectionFollowJpaRepository.findByCollectionId(collectionId, springPage);

        return PageConverter.toDomain(page, collectionFollowJpaMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID followerId, UUID collectionId) {
        return collectionFollowJpaRepository.existsById(new CollectionFollowJpaId(followerId, collectionId));
    }

    @Override
    public void deleteById(UUID followerId, UUID collectionId) {
        collectionFollowJpaRepository.deleteById(new CollectionFollowJpaId(followerId, collectionId));
    }
}
