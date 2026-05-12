package com.collectto.api_collectto.presentation.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.auth.ProcessUserLoginUseCase;
import com.collectto.api_collectto.infrastructure.persistence.shared.TransactionalProxy;
import com.collectto.api_collectto.infrastructure.security.SpringSecurityAuthentication;
import com.collectto.api_collectto.presentation.dto.auth.LoginRequest;
import com.collectto.api_collectto.presentation.dto.auth.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final SpringSecurityAuthentication springSecurityAuthentication;
    private final ProcessUserLoginUseCase processUserLoginUseCase;
    private final TransactionalProxy transactionalProxy;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate JWT token", description = "Validates user credentials and returns a JWT access token for authenticated sessions.")
    public LoginResponse login(@RequestBody LoginRequest request) {
        var user = springSecurityAuthentication.authenticate(request.email(), request.password());
        var token = transactionalProxy.execute(() ->  processUserLoginUseCase.execute(user));
        return new LoginResponse(token); // Implement response entity later
    }
}
