package com.collectto.api_collectto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.collectto.api_collectto.application.usecases.userfollow.AcceptFollowUseCase;
import com.collectto.api_collectto.application.usecases.userfollow.DeclineFollowUseCase;
import com.collectto.api_collectto.application.usecases.userfollow.FollowUserUseCase;
import com.collectto.api_collectto.application.usecases.userfollow.UnfollowUserUseCase;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;

@Configuration
public class UserFollowConfig {

    @Bean
    public AcceptFollowUseCase acceptFollowUseCase(UserFollowRepository userFollowRepository, UserRepository userRepository, 
        NotificationRepository notificationRepository) {
        return new AcceptFollowUseCase(userFollowRepository, userRepository, notificationRepository);
    }

    @Bean
    public DeclineFollowUseCase declineFollowUseCase(UserFollowRepository userFollowRepository, NotificationRepository notificationRepository) {
        return new DeclineFollowUseCase(userFollowRepository, notificationRepository);
    }

    @Bean
    public FollowUserUseCase followUserUseCase(UserFollowRepository userFollowRepository, NotificationRepository notificationRepository) {
        return new FollowUserUseCase(userFollowRepository, notificationRepository);
    }

    @Bean
    public UnfollowUserUseCase unfollowUserUseCase(UserFollowRepository userFollowRepository, UserRepository userRepository, 
        NotificationRepository notificationRepository) {
        return new UnfollowUserUseCase(userFollowRepository, userRepository, notificationRepository);
    }
}
