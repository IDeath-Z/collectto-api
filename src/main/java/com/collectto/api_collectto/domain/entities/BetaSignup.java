package com.collectto.api_collectto.domain.entities;

import java.time.Instant;

import com.collectto.api_collectto.domain.enums.PreRegisterOrigin;
import com.collectto.api_collectto.domain.shared.DomainValidator;

public final class BetaSignup {

    Long id;
    String name;
    String email;
    String collects;
    PreRegisterOrigin source;
    Instant createdAt;

    public BetaSignup(Long id, String name, String email, String collects, PreRegisterOrigin source, Instant createdAt) {
        this.id = id;
        this.name = DomainValidator.requireNonNull(name, "Name cannot be null");
        this.email = DomainValidator.requireNonNull(email, "Email cannot be null");
        this.collects = collects;
        this.source = source;
        this.createdAt = DomainValidator.requireNonNull(createdAt, "Created at timestamp cannot be null");
    }

    public static BetaSignup create(String name, String email, String collects, PreRegisterOrigin source) {
        return new BetaSignup(null, name, email, collects, source, Instant.now());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCollects() {
        return collects;
    }

    public PreRegisterOrigin getSource() {
        return source;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
