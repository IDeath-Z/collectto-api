package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.itemcomment.CommentItemUseCase;
import com.collectto.api_collectto.application.usecases.itemcomment.DeleteCommentUseCase;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemCommentRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;

@Configuration
public class ItemCommentConfig {

    @Bean
    public CommentItemUseCase commentItemUseCase(ItemCommentRepository itemCommentRepository, ItemRepository itemRepository, CollectionRepository collectionRepository) {
        return new CommentItemUseCase(itemCommentRepository, itemRepository, collectionRepository);
    }

    @Bean
    public DeleteCommentUseCase deleteCommentUseCase(ItemCommentRepository itemCommentRepository, ItemRepository itemRepository) {
        return new DeleteCommentUseCase(itemCommentRepository, itemRepository);
    }
}
