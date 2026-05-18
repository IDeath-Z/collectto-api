package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.explore.FetchExploreUseCase;
import com.collectto.api_collectto.domain.ports.ExploreRepository;

@Configuration
public class ExploreConfig {

    @Bean
    public FetchExploreUseCase fetchExploreUseCase(ExploreRepository exploreRepository) {
        return new FetchExploreUseCase(exploreRepository);
    }
}
