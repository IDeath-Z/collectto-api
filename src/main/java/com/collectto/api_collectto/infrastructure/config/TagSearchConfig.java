package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.search.FetchTagSearchUseCase;
import com.collectto.api_collectto.domain.ports.TagRepository;

@Configuration
public class TagSearchConfig {

    @Bean
    public FetchTagSearchUseCase fetchTagSearchUseCase(TagRepository tagRepository) {
        return new FetchTagSearchUseCase(tagRepository);
    }
}
