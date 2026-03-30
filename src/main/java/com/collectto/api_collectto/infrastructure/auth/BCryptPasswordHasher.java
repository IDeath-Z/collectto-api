package com.collectto.api_collectto.infrastructure.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.collectto.api_collectto.domain.ports.PasswordHasher;

@Component
public class BCryptPasswordHasher implements PasswordHasher {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public String hash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean verify(String password, String hash) {
        return encoder.matches(password, hash);
    }

}
