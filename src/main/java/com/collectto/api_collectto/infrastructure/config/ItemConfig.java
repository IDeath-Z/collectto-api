package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.item.CreateItemUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;

@Configuration
public class ItemConfig {

    @Bean
    public CreateItemUseCase createItemUseCase(ItemRepository itemRepository, CollectionRepository collectionRepository, StorageProvider storageProvider) {
        return new CreateItemUseCase(itemRepository, collectionRepository, storageProvider);
    }
}
