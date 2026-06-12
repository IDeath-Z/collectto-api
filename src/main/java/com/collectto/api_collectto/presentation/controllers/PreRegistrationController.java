package com.collectto.api_collectto.presentation.controllers;

import com.collectto.api_collectto.infrastructure.persistence.shared.TransactionalProxy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.preregistration.RegisterForBetaUseCase;
import com.collectto.api_collectto.domain.enums.PreRegisterOrigin;
import com.collectto.api_collectto.presentation.dto.preregistration.PreRegistrationRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preregister")
public class PreRegistrationController {

    private final TransactionalProxy transactionalProxy;
    private final RegisterForBetaUseCase registerForBetaUseCase;

    @PostMapping
    public ResponseEntity<Void> register(@Valid @RequestBody PreRegistrationRequest request) {
        
        
        transactionalProxy.execute(() -> {
            registerForBetaUseCase.execute(new RegisterForBetaUseCase.Input(
                request.name(),
                request.email(),
                request.collects(),
                PreRegisterOrigin.MOBI2026
            ));
        });

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}