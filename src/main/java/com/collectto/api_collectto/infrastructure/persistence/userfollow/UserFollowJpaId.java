package com.collectto.api_collectto.infrastructure.persistence.userfollow;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserFollowJpaId implements Serializable {

    @Column(name = "follower_id")
    private UUID followerId;

    @Column(name = "followed_id")
    private UUID followedId;
}
