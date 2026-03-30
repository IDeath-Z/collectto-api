package com.collectto.api_collectto.presentation.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank @Email @Schema(description = "User email address", example = "maria@email.com", format = "email") 
    String email,
            
    @NotBlank @Schema(description = "User plain-text password", example = "StrongPassword@123") 
    String password
) {}
