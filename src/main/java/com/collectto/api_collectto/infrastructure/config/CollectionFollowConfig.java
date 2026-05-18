package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.collectionfollow.FollowCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collectionfollow.UnfollowCollectionUseCase;
import com.collectto.api_collectto.domain.ports.CollectionFollowRepository;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;

@Configuration
public class CollectionFollowConfig {

    @Bean
    public FollowCollectionUseCase createCollectionFollowUseCase(CollectionFollowRepository collectionFollowRepository, 
        CollectionRepository collectionRepository, NotificationRepository notificationRepository) {
        return new FollowCollectionUseCase(collectionFollowRepository, collectionRepository, notificationRepository);
    }

    @Bean
    public UnfollowCollectionUseCase createUnfollowCollectionUseCase(CollectionFollowRepository collectionFollowRepository, 
        CollectionRepository collectionRepository, NotificationRepository notificationRepository) {
        return new UnfollowCollectionUseCase(collectionFollowRepository, collectionRepository, notificationRepository);
    }
}
