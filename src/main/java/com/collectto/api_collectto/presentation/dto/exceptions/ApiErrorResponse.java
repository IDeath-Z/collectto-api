package com.collectto.api_collectto.presentation.dto.exceptions;

import java.time.Instant;

public record ApiErrorResponse(
    Instant timestamp,
    Integer status,
    String error,
    String message,
    String path
) {}