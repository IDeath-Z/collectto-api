package com.collectto.api_collectto.presentation.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ChangePasswordRequest", description = "Payload for changing user password")
public record ChangePasswordRequest(
    @Schema(description = "Current plain-text password") 
    String currentPassword,
            
    @Schema(description = "New plain-text password") 
    String newPassword
) {}
