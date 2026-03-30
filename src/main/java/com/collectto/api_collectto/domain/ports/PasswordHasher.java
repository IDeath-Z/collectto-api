package com.collectto.api_collectto.domain.ports;

public interface PasswordHasher {
    String hash(String password);
    boolean verify(String password, String hash);
}
