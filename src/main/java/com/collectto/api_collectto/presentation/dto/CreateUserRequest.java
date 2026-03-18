package com.collectto.api_collectto.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateUserRequest", description = "Payload for user creation")
public record CreateUserRequest(
   
    @Schema(description = "User full name", example = "Maria Silva") 
    String name,
            
    @Schema(description = "Unique username (no spaces)", example = "maria_silva") 
    String username,
            
    @Schema(description = "User email address", example = "maria@email.com", format = "email") 
    String email,
            
    @Schema(description = "Plain-text password (will be hashed on the backend)", example = "StrongPassword@123") 
    String password,
            
    @Schema(description = """
                Birth date as an ISO-8601 string.
                Accepted format: yyyy-MM-dd (e.g., 2002-07-15).
                """, example = "2002-07-15", format = "date", pattern = "yyyy-MM-dd") 
    String birthdayDate
) {}
