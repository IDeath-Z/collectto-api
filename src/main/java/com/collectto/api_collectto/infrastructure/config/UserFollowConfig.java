package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.userfollow.CreateUserFollowUseCase;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;

@Configuration
public class UserFollowConfig {

    @Bean
    public CreateUserFollowUseCase createUserFollowUseCase(UserFollowRepository userFollowRepository) {
        return new CreateUserFollowUseCase(userFollowRepository);
    }
}
