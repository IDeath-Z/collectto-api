package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.search.FetchGlobalSearchUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Configuration
public class GlobalSearchConfig {

    @Bean
    public FetchGlobalSearchUseCase fetchGlobalSearchUseCase(UserRepository userRepository, CollectionRepository collectionRepository, ItemRepository itemRepository) {
        return new FetchGlobalSearchUseCase(userRepository, collectionRepository, itemRepository);
    }

}
