package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.feed.FetchFeedUseCase;
import com.collectto.api_collectto.domain.ports.FeedRepository;

@Configuration
public class FeedConfig {

    @Bean
    public FetchFeedUseCase fetchFeedUseCase(FeedRepository feedRepository) {
        return new FetchFeedUseCase(feedRepository);
    }
}
