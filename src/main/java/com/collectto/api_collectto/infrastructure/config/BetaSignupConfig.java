package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.preregistration.RegisterForBetaUseCase;
import com.collectto.api_collectto.domain.ports.BetaSignupRepository;

@Configuration
public class BetaSignupConfig {

    @Bean
    public RegisterForBetaUseCase RegisterForBetaUseCase(BetaSignupRepository betaSignupRepository) {
        return new RegisterForBetaUseCase(betaSignupRepository);
    }

}
