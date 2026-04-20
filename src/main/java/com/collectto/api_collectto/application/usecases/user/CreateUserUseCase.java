package com.collectto.api_collectto.application.usecases.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.collectto.api_collectto.application.exceptions.EmailAlreadyExistsException;
import com.collectto.api_collectto.application.exceptions.UsernameAlreadyExistsException;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.ports.PasswordHasher;
import com.collectto.api_collectto.domain.ports.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public record Input(String name, String username, String email, String password, String birthdayDate) {}
    public record Output(String id, String name, String username, String email, String bio, String profilePictureUrl,
            int followersCount, int followingCount, boolean isActive, String birthdayDate, String creationDate) {
    }
            
    public Output execute(Input input) {
        if (userRepository.existsByEmail(input.email()))
            throw new EmailAlreadyExistsException(input.email());
        if (userRepository.existsByUsername(input.username()))
            throw new UsernameAlreadyExistsException(input.username());

        User user = new User(
                UUID.randomUUID(),
                input.name(),
                input.username(),
                input.email(),
                passwordHasher.hash(input.password()),
                null,
                null,
                0,
                0,
                true,
                LocalDate.parse(input.birthdayDate()),
                Instant.now());

        User savedUser = userRepository.save(user);

        return new Output(
                savedUser.getId().toString(),
                savedUser.getName(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getBio(),
                savedUser.getProfilePictureUrl(),
                savedUser.getFollowersCount(),
                savedUser.getFollowingCount(),
                savedUser.isActive(),
                savedUser.getBirthdayDate().toString(),
                savedUser.getCreationDate().toString());
    }

}
