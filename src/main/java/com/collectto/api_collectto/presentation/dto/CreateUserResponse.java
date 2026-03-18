package com.collectto.api_collectto.presentation.dto;

public record CreateUserResponse(
    String id,
    String name,
    String username,
    String email,
    String createdAt
) {}
