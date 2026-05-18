package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.itemcomment.CommentItemUseCase;
import com.collectto.api_collectto.application.usecases.itemcomment.DeleteCommentUseCase;
import com.collectto.api_collectto.application.usecases.itemcomment.FetchItemCommentsUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemCommentRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Configuration
public class ItemCommentConfig {

    @Bean
    public CommentItemUseCase commentItemUseCase(ItemCommentRepository itemCommentRepository, ItemRepository itemRepository, 
        CollectionRepository collectionRepository, NotificationRepository notificationRepository) {
        return new CommentItemUseCase(itemCommentRepository, itemRepository, collectionRepository, notificationRepository);
    }

    @Bean
    public DeleteCommentUseCase deleteCommentUseCase(ItemCommentRepository itemCommentRepository, ItemRepository itemRepository, 
        NotificationRepository notificationRepository) {
        return new DeleteCommentUseCase(itemCommentRepository, itemRepository, notificationRepository);
    }

    @Bean
    public FetchItemCommentsUseCase fetchItemCommentsUseCase(ItemCommentRepository itemCommentRepository, CollectionRepository collectionRepository, 
        ItemRepository itemRepository, UserRepository userRepository) {
        return new FetchItemCommentsUseCase(itemCommentRepository, collectionRepository, itemRepository, userRepository);
    }
}
