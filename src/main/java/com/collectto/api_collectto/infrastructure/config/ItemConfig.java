package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.item.CreateItemUseCase;
import com.collectto.api_collectto.application.usecases.item.FetchCollectionItemsUseCase;
import com.collectto.api_collectto.application.usecases.item.FetchItemUseCase;
import com.collectto.api_collectto.application.usecases.item.UpdateItemUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.shared.StorageUrlPaths;

@Configuration
public class ItemConfig {

    @Bean
    public CreateItemUseCase createItemUseCase(ItemRepository itemRepository, CollectionRepository collectionRepository, StorageProvider storageProvider, StorageUrlPaths storageUrlPaths) {
        return new CreateItemUseCase(itemRepository, collectionRepository, storageProvider, storageUrlPaths);
    }

    @Bean
    public FetchCollectionItemsUseCase fetchCollectionItemsUseCase(ItemRepository itemRepository, CollectionRepository collectionRepository) {
        return new FetchCollectionItemsUseCase(itemRepository, collectionRepository);
    }

    @Bean
    public FetchItemUseCase fetchItemUseCase(ItemRepository itemRepository, CollectionRepository collectionRepository) {
        return new FetchItemUseCase(itemRepository, collectionRepository);
    }

    @Bean
    public UpdateItemUseCase updateItemUseCase(ItemRepository itemRepository, StorageProvider storageProvider, StorageUrlPaths storageUrlPaths) {
        return new UpdateItemUseCase(itemRepository, storageProvider, storageUrlPaths);
    }
}
