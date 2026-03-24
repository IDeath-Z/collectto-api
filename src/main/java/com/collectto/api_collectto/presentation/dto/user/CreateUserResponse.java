package com.collectto.api_collectto.presentation.dto.user;

public record CreateUserResponse(
    String id,
    String name,
    String username,
    String email,
    String createdAt
) {}
