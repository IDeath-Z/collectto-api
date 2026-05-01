package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.collection.CreateCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collection.FetchCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collection.FetchUserCollectionsUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;

@Configuration
public class CollectionConfig {

    @Bean
    public CreateCollectionUseCase createCollectionUseCase(CollectionRepository collectionRepository, StorageProvider storageProvider) {
        return new CreateCollectionUseCase(collectionRepository, storageProvider);
    }

    @Bean
    public FetchCollectionUseCase fetchCollectionUseCase(CollectionRepository collectionRepository) {
        return new FetchCollectionUseCase(collectionRepository);
    }

    @Bean
    public FetchUserCollectionsUseCase fetchUserCollectionsUseCase(CollectionRepository collectionRepository, ItemRepository itemRepository) {
        return new FetchUserCollectionsUseCase(collectionRepository, itemRepository);
    }
}
