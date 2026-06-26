package com.collectto.api_collectto.infrastructure.persistence.preregistration;

import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.entities.BetaSignup;

@Component
public final class BetaSignupMapper {

    public BetaSignupJpaEntity toJpa(BetaSignup betaSignup) {
        BetaSignupJpaEntity jpaEntity = new BetaSignupJpaEntity();
        jpaEntity.setId(betaSignup.getId());
        jpaEntity.setName(betaSignup.getName());
        jpaEntity.setEmail(betaSignup.getEmail());
        jpaEntity.setCollects(betaSignup.getCollects());
        jpaEntity.setSource(betaSignup.getSource());
        jpaEntity.setCreatedAt(betaSignup.getCreatedAt());
        return jpaEntity;
    }

    public BetaSignup toDomain(BetaSignupJpaEntity entity) {
        return new BetaSignup(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getCollects(),
            entity.getSource(),
            entity.getCreatedAt()
        );
    }
}
