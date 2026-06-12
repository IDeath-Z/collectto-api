package com.collectto.api_collectto.infrastructure.persistence.preregistration;

import org.springframework.stereotype.Repository;

import com.collectto.api_collectto.domain.entities.BetaSignup;
import com.collectto.api_collectto.domain.ports.BetaSignupRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BetaSignupRepositoryAdapter implements BetaSignupRepository {

    private final BetaSignupJpaRepository jpaRepository;
    private final BetaSignupMapper mapper;

    public BetaSignup save(BetaSignup betaSignup) {
        BetaSignupJpaEntity jpaEntity = mapper.toJpa(betaSignup);
        BetaSignupJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomain(savedEntity);
    }

    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
