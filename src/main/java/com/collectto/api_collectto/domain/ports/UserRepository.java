package com.collectto.api_collectto.domain.ports;

import java.util.Optional;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.User;

public interface UserRepository {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User save(User user);
    Optional<User> findByID(UUID id);
    Optional<User> findByEmail(String email);
}
