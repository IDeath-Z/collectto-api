package com.collectto.api_collectto.infrastructure.persistence.userfollow;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.UserFollow;

@Component
public class UserFollowMapper {

    public UserFollowJpaEntity toJpa(UserFollow userFollow) {
        UserFollowJpaId id = new UserFollowJpaId(userFollow.getFollowerId(), userFollow.getFollowedId());

        UserFollowJpaEntity entity = new UserFollowJpaEntity();
        entity.setId(id);
        entity.setStatus(userFollow.getStatus());
        entity.setCreatedAt(userFollow.getCreatedAt());
        return entity;
    }

    public UserFollow toDomain(UserFollowJpaEntity entity) {
        return new UserFollow(
            entity.getId().getFollowerId(),
            entity.getId().getFollowedId(),
            entity.getStatus(),
            entity.getCreatedAt()
        );
    }
}
