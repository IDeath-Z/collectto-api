package com.collectto.api_collectto.infrastructure.persistence.userfollow;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.UserFollow;
import com.collectto.api_collectto.domain.enums.FollowStatus;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserFollowRepositoryAdapter implements UserFollowRepository {

    private final UserFollowJpaRepository userFollowJpaRepository;
    private final UserFollowMapper userFollowMapper;

    @Override
    public Optional<UserFollow> findById(UUID followerId, UUID followedId) {
        return userFollowJpaRepository.findById(new UserFollowJpaId(followerId, followedId)).map(userFollowMapper::toDomain);
    }

    @Override
    public UserFollow save(UserFollow userFollow) {
        UserFollowJpaEntity entity = userFollowMapper.toJpa(userFollow);
        return userFollowMapper.toDomain(userFollowJpaRepository.save(entity));
    }

    @Override
    public DomainPageResult<UserFollow> findByFollowerIdAndStatus(UUID followerId, FollowStatus status, DomainPageRequest pageRequest) {
        Sort sort = pageRequest.sortBy().getDirection().equals("ASC")
        ? Sort.by(pageRequest.sortBy().getField()).ascending()
        : Sort.by(pageRequest.sortBy().getField()).descending();

        PageRequest springPage = PageRequest.of(pageRequest.page(), pageRequest.size(), sort);
        Page<UserFollowJpaEntity> page = userFollowJpaRepository.findByFollowerIdAndStatus(followerId, status, springPage);

        return new DomainPageResult<>(
            page.getContent().stream().map(userFollowMapper::toDomain).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    public DomainPageResult<UserFollow> findByFollowedIdAndStatus(UUID followedId, FollowStatus status, DomainPageRequest pageRequest) {
        Sort sort = pageRequest.sortBy().getDirection().equals("ASC")
        ? Sort.by(pageRequest.sortBy().getField()).ascending()
        : Sort.by(pageRequest.sortBy().getField()).descending();

        PageRequest springPage = PageRequest.of(pageRequest.page(), pageRequest.size(), sort);
        Page<UserFollowJpaEntity> page = userFollowJpaRepository.findByFollowedIdAndStatus(followedId, status, springPage);

        return new DomainPageResult<>(
            page.getContent().stream().map(userFollowMapper::toDomain).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    public void deleteById(UUID followerId, UUID followedId) {
        userFollowJpaRepository.deleteById(new UserFollowJpaId(followerId, followedId));
    }
}
