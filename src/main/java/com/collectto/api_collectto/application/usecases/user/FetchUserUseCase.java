package com.collectto.api_collectto.application.usecases.user;

import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class FetchUserUseCase {

    private final UserRepository userRepository;

    public record Input(UUID userId) {}
    public record Output(UUID id, String name, String username, String bio, String profilePictureUrl,
        String profileBackgroundUrl, int followersCount, int followingCount, String creationDate) {}

    public Output execute(Input input) {
        User user = userRepository.findById(input.userId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + input.userId())); //Create custom exception as needed

        if (!user.isActive())
            throw new RuntimeException("User with id " + input.userId() + " is deactivated."); //Create custom exception as needed

        return new Output(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getBio(),
            user.getProfilePictureUrl(),
            user.getProfileBackgroundUrl(),
            user.getFollowersCount(),
            user.getFollowingCount(),
            user.getCreationDate().toString()
        );
    }
}
