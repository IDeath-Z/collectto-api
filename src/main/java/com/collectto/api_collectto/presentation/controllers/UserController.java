package com.collectto.api_collectto.presentation.controllers;

import java.util.UUID;

import com.collectto.api_collectto.application.usecases.user.UpdateUserUseCase;
import com.collectto.api_collectto.application.usecases.userfollow.CreateUserFollowUseCase;
import com.collectto.api_collectto.infrastructure.persistence.shared.TransactionalProxy;
import com.collectto.api_collectto.infrastructure.security.SecurityUserDetails;
import com.collectto.api_collectto.presentation.dto.user.UpdateUserRequest;
import com.collectto.api_collectto.presentation.dto.user.UserFollowResponse;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.collectto.api_collectto.application.usecases.user.CreateUserUseCase;
import com.collectto.api_collectto.application.usecases.user.FetchUserUseCase;
import com.collectto.api_collectto.presentation.dto.user.CreateUserRequest;
import com.collectto.api_collectto.presentation.dto.user.CreateUserResponse;
import com.collectto.api_collectto.presentation.dto.user.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FetchUserUseCase fetchUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final CreateUserFollowUseCase createUserFollowUseCase;
    private final TransactionalProxy transactionalProxy;

    @PostMapping("create")
    @Operation(summary = "Create a new user", description = "Registers a new user in the system with the provided details.")
    public CreateUserResponse create(@RequestBody @Valid CreateUserRequest request) {
        var output = transactionalProxy.execute(() -> createUserUseCase.execute(
            new CreateUserUseCase.Input(
                request.name(),
                request.username(),
                request.email(),
                request.password(),
                request.birthdayDate()
            )
        ));

        return new CreateUserResponse(
            output.id(),
            output.name(),
            output.username(),
            output.email(),
            output.creationDate()
        ); // Implement response entity later
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Fetch user details", description = "Retrieves the details of a user by their unique identifier.")
    public UserResponse get(@PathVariable UUID userId) {
        var output = transactionalProxy.executeReadOnly(() -> fetchUserUseCase.execute(new FetchUserUseCase.Input(userId)));

        return new UserResponse(
            output.id(),
            output.name(),
            output.username(),
            output.email(),
            output.bio(),
            output.profilePictureUrl(),
            output.profileBackgroundUrl(),
            output.followersCount(),
            output.followingCount(),
            output.isActive(),
            output.birthdayDate(),
            output.creationDate()
        );
    }

    @PatchMapping("/profile")
    @Operation(summary = "Update user profile", description = "Updates the profile of the authenticated user. Only the provided fields will be updated.")
    public UserResponse patch(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid UpdateUserRequest request) {
        UUID userId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateUserUseCase.execute(
            new UpdateUserUseCase.Input(
                userId,
                request.name(),
                request.username(),
                request.bio(),
                request.profilePictureUrl(),
                request.profileBackgroundUrl(),
                request.birthdayDate()
            )
        ));

        return new UserResponse(
            output.id(),
            output.name(),
            output.username(),
            output.email(),
            output.bio(),
            output.profilePictureUrl(),
            output.profileBackgroundUrl(),
            output.followersCount(),
            output.followingCount(),
            output.isActive(),
            output.birthdayDate(),
            output.creationDate()
        );
    }

    // Follow
    @PostMapping("/{followedId}/follow")
    @Operation(summary = "Follow a user", description = "Sends a follow request to the specified user. If the request is successful, the follow status will be PENDING until accepted by the followed user.")
    public UserFollowResponse followUser(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID followedId) {
        UUID userId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createUserFollowUseCase.execute(
            new CreateUserFollowUseCase.Input(userId, followedId)
        ));

        return new UserFollowResponse(
            output.followerId(), 
            output.followedId(), 
            output.status(), 
            output.createdAt()
        );
    }  
}
