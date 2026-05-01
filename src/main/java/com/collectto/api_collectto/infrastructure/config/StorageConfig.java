package com.collectto.api_collectto.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.storage.GenerateUploadUrlsUseCase;
import com.collectto.api_collectto.domain.ports.StorageProvider;

@Configuration
public class StorageConfig {

    @Value("${storage.path.profilePicture}")
    private String profilePicture;

    @Value("${storage.path.profileBackground}")
    private String profileBackground;

    @Value("${storage.path.collections}")
    private String collectionsPath;

    @Value("${storage.path.items}")
    private String itemsPath;

    @Value("${storage.presignedUrlExpirationMinutes}")
    private int presignedUrlExpirationMinutes;

    @Bean
    public GenerateUploadUrlsUseCase generateUploadUrlsUseCase(StorageProvider storageProvider) {
        return new GenerateUploadUrlsUseCase(storageProvider, profilePicture, profileBackground, collectionsPath, itemsPath, presignedUrlExpirationMinutes);
    }
}
