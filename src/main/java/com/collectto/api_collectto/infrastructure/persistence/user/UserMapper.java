package com.collectto.api_collectto.infrastructure.persistence.user;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.User;

@Component
public class UserMapper {

    public UserJpaEntity toJpa(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setBio(user.getBio());
        entity.setProfilePictureUrl(user.getProfilePictureUrl());
        entity.setProfileBackgroundUrl(user.getProfileBackgroundUrl());
        entity.setFollowersCount(user.getFollowersCount());
        entity.setFollowingCount(user.getFollowingCount());
        entity.setActive(user.isActive());
        entity.setBirthdayDate(user.getBirthdayDate());
        entity.setCreatedAt(user.getCreationDate());
        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        return new User(
            entity.getId(),
            entity.getName(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPasswordHash(),
            entity.getBio(),
            entity.getProfilePictureUrl(),
            entity.getProfileBackgroundUrl(),
            entity.getFollowersCount(),
            entity.getFollowingCount(),
            entity.isActive(),
            entity.getBirthdayDate(),
            entity.getCreatedAt()
        );
    }
}
