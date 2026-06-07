package com.collectto.api_collectto.presentation.dto.user;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateUserResponse(
    @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000") 
    UUID id,
            
    @Schema(description = "User full name", example = "Maria Silva") 
    String name,
            
    @Schema(description = "Unique username (no spaces)", example = "maria_silva") 
    String username,
            
    @Schema(description = "User email address", example = "maria@email.com", format = "email")
    String email,
                        
    @Schema(description = "Timestamp of user creation", example = "2026-01-01T00:00:00Z") 
    Instant createdAt
) {}
