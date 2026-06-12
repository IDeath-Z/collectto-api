package com.collectto.api_collectto.presentation.dto.preregistration;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PreRegistrationRequest(
    @NotBlank @Schema(description = "The name of the user")
    String name,

    @NotBlank @Email @Schema(description = "The email of the user")
    String email,

    @Schema(description = "The collects of the user") 
    String collects
) {}
