package com.collectto.api_collectto.presentation.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.user.CreateUserUseCase;
import com.collectto.api_collectto.presentation.dto.user.CreateUserRequest;
import com.collectto.api_collectto.presentation.dto.user.CreateUserResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping("create")
    @Operation(summary = "Create a new user", description = "Registers a new user in the system with the provided details.")
    public CreateUserResponse create(@RequestBody CreateUserRequest request) {
        var output = createUserUseCase.execute(new CreateUserUseCase.Input(
            request.name(),
            request.username(),
            request.email(),
            request.password(),
            request.birthdayDate()
        ));

        return new CreateUserResponse(
            output.id(),
            output.name(),
            output.username(),
            output.email(),
            output.creationDate()
        ); // Implement response entity later
    }   
}
