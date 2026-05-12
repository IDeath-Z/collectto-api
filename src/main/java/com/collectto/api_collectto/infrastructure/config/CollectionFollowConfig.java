package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.collectionfollow.FollowCollectionUseCase;
import com.collectto.api_collectto.domain.ports.CollectionFollowRepository;
import com.collectto.api_collectto.domain.ports.CollectionRepository;

@Configuration
public class CollectionFollowConfig {

    @Bean
    public FollowCollectionUseCase createCollectionFollowUseCase(CollectionFollowRepository collectionFollowRepository, CollectionRepository collectionRepository) {
        return new FollowCollectionUseCase(collectionFollowRepository, collectionRepository);
    }
}
