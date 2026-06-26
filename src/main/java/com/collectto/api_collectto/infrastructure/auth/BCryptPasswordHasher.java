package com.collectto.api_collectto.infrastructure.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.ports.PasswordHasher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class BCryptPasswordHasher implements PasswordHasher {

    private final BCryptPasswordEncoder encoder;

    @Override
    public String hash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean verify(String password, String hash) {
        return encoder.matches(password, hash);
    }

}
