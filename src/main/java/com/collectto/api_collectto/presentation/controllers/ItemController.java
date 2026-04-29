package com.collectto.api_collectto.presentation.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.item.CreateItemUseCase;
import com.collectto.api_collectto.infrastructure.security.SecurityUserDetails;
import com.collectto.api_collectto.presentation.dto.item.CreateItemRequest;
import com.collectto.api_collectto.presentation.dto.item.CreateItemResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;

    @PostMapping(value = "/create")
    @Operation(summary = "Create a new item", description = "Registers a new item in the system with the provided details.")
    public CreateItemResponse create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateItemRequest request) {
        UUID userId = userDetails.getUser().getId();

        var output = createItemUseCase.execute(new CreateItemUseCase.Input(
            request.collectionId(),
            userId,
            request.name(),
            request.description(),
            request.acquisitionDate(),
            request.lastUsedDate(),
            request.imageFilesUrls(),
            request.attributes(),
            request.tags()
        ));
        
        return new CreateItemResponse(
            output.id(),
            output.collectionId(),
            output.userId(),
            output.name(),
            output.description(),
            output.acquisitionDate(),
            output.lastUsedDate(),
            output.imageFilesUrls(),
            output.attributes(),
            output.tags(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt()
        );
    }
}
