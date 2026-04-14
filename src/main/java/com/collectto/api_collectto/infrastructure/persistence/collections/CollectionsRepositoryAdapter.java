package com.collectto.api_collectto.infrastructure.persistence.collections;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.Collections;
import com.collectto.api_collectto.domain.ports.CollectionsRepository;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaRepository;

@Repository
public class CollectionsRepositoryAdapter implements CollectionsRepository {

    @Autowired
    private CollectionsJpaRepository collectionsJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private CollectionsMapper collectionsMapper;

    @Override
    public List<Collections> findByUserId(UUID userId) {
        return collectionsJpaRepository.findByUserId(userId).stream()
            .map(collectionsMapper::toDomain)
            .toList();
    }

    @Override
    public Collections save(Collections collection) {
        UserJpaEntity user = userJpaRepository.findById(collection.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + collection.getUserId())); // Implement better error handling as needed

        return collectionsMapper.toDomain(collectionsJpaRepository.save(collectionsMapper.toJpa(collection, user)));
    }

    @Override
    public Optional<Collections> findById(UUID id) {
        return collectionsJpaRepository.findById(id).map(collectionsMapper::toDomain);
    }

    @Override
    public List<Collections> findByUserIdAndName(UUID userId, String name) {
        return collectionsJpaRepository.findByUserIdAndNameContainingIgnoreCase(userId, name).stream()
            .map(collectionsMapper::toDomain)
            .toList();
    }


}
