package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.user.CreateUserUseCase;
import com.collectto.api_collectto.application.usecases.user.FetchUserUseCase;
import com.collectto.api_collectto.application.usecases.user.UpdateUserUseCase;
import com.collectto.api_collectto.domain.ports.PasswordHasher;
import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Configuration
public class UserConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new CreateUserUseCase(userRepository, passwordHasher);
    }

    @Bean
    public FetchUserUseCase fetchUserUseCase(UserRepository userRepository) {
        return new FetchUserUseCase(userRepository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository, StorageProvider storageProvider) {
        return new UpdateUserUseCase(userRepository, storageProvider);
    }
}
