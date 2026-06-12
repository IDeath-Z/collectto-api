package com.collectto.api_collectto.infrastructure.persistence.preregistration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BetaSignupJpaRepository extends JpaRepository<BetaSignupJpaEntity, Long> {
      
    boolean existsByEmail(String email);
}