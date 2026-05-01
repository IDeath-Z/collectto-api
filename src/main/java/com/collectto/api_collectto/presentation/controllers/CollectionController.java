package com.collectto.api_collectto.presentation.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.collection.CreateCollectionUseCase;
import com.collectto.api_collectto.infrastructure.security.SecurityUserDetails;
import com.collectto.api_collectto.presentation.dto.collection.CreateCollectionRequest;
import com.collectto.api_collectto.presentation.dto.collection.CollectionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectionController {

    private final CreateCollectionUseCase createCollectionUseCase;

    @PostMapping(value = "/create")
    @Operation(summary = "Create a new collection", description = "Registers a new collection in the system with the provided details.")
    public CollectionResponse create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateCollectionRequest request) {
        UUID userId = userDetails.getUser().getId();

        var output = createCollectionUseCase.execute(new CreateCollectionUseCase.Input(
            userId,
            request.name(),
            request.description(),
            request.coverImageUrl(),
            request.tags()
        ));
        
        return new CollectionResponse(
                output.id(),
                output.userId(),
                output.name(),
                output.description(),
                output.coverImageURL(),
                output.visibility(),
                output.followersCount(),
                output.tags(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt()
        );
    }
    
}
