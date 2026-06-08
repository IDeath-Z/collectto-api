package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.auth.ProcessUserLoginUseCase;
import com.collectto.api_collectto.application.usecases.auth.RefreshSessionUseCase;
import com.collectto.api_collectto.application.usecases.auth.ValidateTokenUseCase;
import com.collectto.api_collectto.domain.ports.TokenProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Configuration
public class AuthConfig {

    @Bean
    public ProcessUserLoginUseCase processUserLoginUseCase(TokenProvider tokenProvider, UserRepository userRepository) {
        return new ProcessUserLoginUseCase(tokenProvider, userRepository);
    }

    @Bean
    public RefreshSessionUseCase refreshSessionUseCase(TokenProvider tokenProvider, UserRepository userRepository) {
        return new RefreshSessionUseCase(tokenProvider, userRepository);
    }

    @Bean
    public ValidateTokenUseCase validateTokenUseCase(TokenProvider tokenProvider, UserRepository userRepository) {
        return new ValidateTokenUseCase(tokenProvider, userRepository);
    }
}
