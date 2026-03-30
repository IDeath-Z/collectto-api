package com.collectto.api_collectto.presentation.dto.auth;

public record LoginResponse(
    String accessToken,
    String tokenType) {

    public LoginResponse(String accessToken) {
        this(accessToken, "Bearer");
    }

}