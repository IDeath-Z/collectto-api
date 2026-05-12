package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.itemlike.LikeItemUseCase;
import com.collectto.api_collectto.application.usecases.itemlike.UnlikeItemUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemLikeRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;

@Configuration
public class ItemLikeConfig {

    @Bean
    public LikeItemUseCase likeItemUseCase(ItemLikeRepository itemLikeRepository, ItemRepository itemRepository, CollectionRepository collectionRepository) {
        return new LikeItemUseCase(itemLikeRepository, itemRepository, collectionRepository);
    }

    @Bean
    public UnlikeItemUseCase unlikeItemUseCase(ItemLikeRepository itemLikeRepository, ItemRepository itemRepository) {
        return new UnlikeItemUseCase(itemLikeRepository, itemRepository);
    }
}
