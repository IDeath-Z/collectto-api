package com.collectto.api_collectto.domain.ports;

public interface TokenProvider {
    
    String generateAccessToken(String subject);
    String generateRefreshToken(String subject);
    String validateAccessToken(String token);
    String validateRefreshToken(String token);
}