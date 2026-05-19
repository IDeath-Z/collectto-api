package com.collectto.api_collectto.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

public interface UserRepository {
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAllByIds(List<UUID> ids);
    Optional<User> findByEmail(String email);
    DomainPageResult<User> searchActiveUsers(String query, DomainPageRequest pageRequest);
    void incrementFollowers(UUID userId);
    void incrementFollowing(UUID userId);
    void decrementFollowers(UUID userId);
    void decrementFollowing(UUID userId);
    void deactivateUser(UUID userId);
    void reactivateUser(UUID userId);
}
