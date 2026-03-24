package com.collectto.api_collectto.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.auth.GenerateTokenUseCase;
import com.collectto.api_collectto.infrastructure.security.SpringSecurityAuthentication;
import com.collectto.api_collectto.presentation.dto.auth.LoginRequest;
import com.collectto.api_collectto.presentation.dto.auth.LoginResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SpringSecurityAuthentication springSecurityAuthentication;

    @Autowired
    private GenerateTokenUseCase generateTokenUseCase;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        var user = springSecurityAuthentication.authenticate(request.email(), request.password());
        var token = generateTokenUseCase.execute(user);
        return new LoginResponse(token); // Implement response entity later
    }
    

}
