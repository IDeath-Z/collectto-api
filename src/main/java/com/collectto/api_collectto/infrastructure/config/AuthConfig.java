package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.auth.GenerateTokenUseCase;
import com.collectto.api_collectto.application.usecases.auth.ValidateTokenUseCase;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Configuration
public class AuthConfig {

    @Bean
    public GenerateTokenUseCase generateTokenUseCase(TokenProvider tokenProvider) {
        return new GenerateTokenUseCase(tokenProvider);
    }

    @Bean
    public ValidateTokenUseCase validateTokenUseCase(TokenProvider tokenProvider, UserRepository userRepository) {
        return new ValidateTokenUseCase(tokenProvider, userRepository);
    }

}
