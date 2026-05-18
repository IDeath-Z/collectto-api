package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.itemlike.FetchItemLikesUseCase;
import com.collectto.api_collectto.application.usecases.itemlike.LikeItemUseCase;
import com.collectto.api_collectto.application.usecases.itemlike.UnlikeItemUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemLikeRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Configuration
public class ItemLikeConfig {

    @Bean
    FetchItemLikesUseCase fetchItemLikesUseCase(ItemLikeRepository itemLikeRepository, CollectionRepository collectionRepository, 
        ItemRepository itemRepository, UserRepository userRepository) {
        return new FetchItemLikesUseCase(itemLikeRepository, collectionRepository, itemRepository, userRepository);
    }

    @Bean
    public LikeItemUseCase likeItemUseCase(ItemLikeRepository itemLikeRepository, ItemRepository itemRepository, CollectionRepository collectionRepository,
        NotificationRepository notificationRepository) {
        return new LikeItemUseCase(itemLikeRepository, itemRepository, collectionRepository, notificationRepository);
    }

    @Bean
    public UnlikeItemUseCase unlikeItemUseCase(ItemLikeRepository itemLikeRepository, ItemRepository itemRepository, NotificationRepository notificationRepository) {
        return new UnlikeItemUseCase(itemLikeRepository, itemRepository, notificationRepository);
    }
}