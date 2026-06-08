package com.collectto.api_collectto.domain.shared;

public record AuthToken(
    String accessToken, 
    String refreshToken
) {}

