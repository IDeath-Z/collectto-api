package com.collectto.api_collectto.infrastructure.persistence.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;
import com.collectto.api_collectto.infrastructure.persistence.shared.PageConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = userMapper.toJpa(user);
        UserJpaEntity savedEntity = userJpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findByIdAndIsActiveTrue(id).map(userMapper::toDomain);
    }

    @Override
    public List<User> findAllByIds(List<UUID> ids) {
        return userJpaRepository.findAllById(ids)
            .stream()
            .map(userMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public DomainPageResult<User> searchActiveUsers(String query, DomainPageRequest pageRequest) {
        PageRequest springPage = PageConverter.toSpring(pageRequest);
        Page<UserJpaEntity> page = userJpaRepository.searchActiveUsers(query, springPage);
        
        return PageConverter.toDomain(page, userMapper::toDomain);
    }

    @Override
    public void incrementFollowers(UUID userId) {
        userJpaRepository.incrementFollowers(userId);
    }

    @Override
    public void incrementFollowing(UUID userId) {
        userJpaRepository.incrementFollowing(userId);
    }

    @Override
    public void decrementFollowers(UUID userId) {
        userJpaRepository.decrementFollowers(userId);
    }

    @Override
    public void decrementFollowing(UUID userId) {
        userJpaRepository.decrementFollowing(userId);
    }

    @Override
    public void deactivateUser(UUID userId) {
        userJpaRepository.deactivateUser(userId);
    }

    @Override
    public void reactivateUser(UUID userId) {
        userJpaRepository.reactivateAccount(userId);
    }
}
