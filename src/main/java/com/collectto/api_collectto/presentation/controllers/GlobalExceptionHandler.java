package com.collectto.api_collectto.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceAlreadyExistsException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.application.exceptions.UnauthorizedException;
import com.collectto.api_collectto.presentation.dto.exceptions.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler({BusinessRuleException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException e, HttpServletRequest request) {
        log.warn("Bad request: {}", e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        log.warn("Resource not found: {}", e.getMessage());
        
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ResourceAlreadyExistsException e, HttpServletRequest request) {
        log.warn("Resource already exists: {}", e.getMessage());
        
        HttpStatus status = HttpStatus.CONFLICT;
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException e, HttpServletRequest request) {
        log.warn("Unauthorized access: {}", e.getMessage());
        
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(ForbiddenActionException e, HttpServletRequest request) {
        log.warn("Forbidden action: {}", e.getMessage());
        
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUncaughtException(Exception e, HttpServletRequest request) {
        log.error("Internal server error: {}", e.getMessage(), e);
        
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            "An unexpected error occurred. Please try again later.", 
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}
