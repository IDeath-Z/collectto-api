package com.collectto.api_collectto.infrastructure.persistence.preregistration;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.collectto.api_collectto.domain.enums.PreRegisterOrigin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "beta_signups")
public class BetaSignupJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String collects;

    @Column(nullable = false)
    private PreRegisterOrigin source;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
}
