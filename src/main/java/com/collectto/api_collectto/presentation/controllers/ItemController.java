package com.collectto.api_collectto.presentation.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.item.CreateItemUseCase;
import com.collectto.api_collectto.application.usecases.item.FetchCollectionItemsUseCase;
import com.collectto.api_collectto.application.usecases.item.FetchItemUseCase;
import com.collectto.api_collectto.application.usecases.item.UpdateItemUseCase;
import com.collectto.api_collectto.domain.enums.SortBy;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.infrastructure.security.SecurityUserDetails;
import com.collectto.api_collectto.presentation.dto.item.CreateItemRequest;
import com.collectto.api_collectto.presentation.dto.item.ItemPageResponse;
import com.collectto.api_collectto.presentation.dto.item.ItemResponse;
import com.collectto.api_collectto.presentation.dto.item.UpdateItemRequest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final FetchCollectionItemsUseCase fetchCollectionItemsUseCase;
    private final FetchItemUseCase fetchItemUseCase;
    private final UpdateItemUseCase updateItemUseCase;

    @PostMapping(value = "/create")
    @Operation(summary = "Create a new item", description = "Registers a new item in the system with the provided details.")
    public ItemResponse create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateItemRequest request) {
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
        
        return new ItemResponse(
            output.id(),
            output.collectionId(),
            output.userId(),
            output.name(),
            output.description(),
            output.acquisitionDate(),
            output.lastUsedDate(),
            output.imageFilesUrls(),
            output.attributes(),
            output.likesCount(),
            output.commentsCount(),
            output.tags(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt()
        );
    }

    @GetMapping("/by-collection/{collectionId}")
    @Operation(summary = "Get paginated items by collection", description = "Retrieves a paginated list of items belonging to a specific collection, with optional sorting.")
    public ItemPageResponse getPaginatedItems(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID collectionId, 
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "CREATED_AT_DESC") SortBy sortBy) {

        UUID requesterId = userDetails.getUser().getId();

        var output = fetchCollectionItemsUseCase.execute(new FetchCollectionItemsUseCase.Input(
            collectionId, 
            requesterId, 
            new DomainPageRequest(page, size, sortBy)
        ));

        return new ItemPageResponse(
            output.items().stream()
                .map(item -> new ItemPageResponse.ItemSummaryResponse(
                    item.id(),
                    item.name(),
                    item.imagesURL()
                ))
                .toList(),
            output.totalPages(),
            output.totalItems(),
            output.currentPage()
        );
    }

    @GetMapping("/{collectionId}/{itemId}")
    @Operation(summary = "Fetch item details", description = "Retrieves detailed information about a specific item, including its attributes and media URLs.")
    public ItemResponse getItemById(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID collectionId, @PathVariable UUID itemId) {
        UUID requesterId = userDetails.getUser().getId();

        var output = fetchItemUseCase.execute(new FetchItemUseCase.Input(
            itemId,
            collectionId,
            requesterId
        ));

        return new ItemResponse(
            output.id(),
            output.collectionId(),
            output.userId(),
            output.name(),
            output.description(),
            output.acquisitionDate(),
            output.lastUsedDate(),
            output.imageFilesUrls(),
            output.attributes(),
            output.likesCount(),
            output.commentsCount(),
            output.tags(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt()
        );
    }

    @PatchMapping("/update")
    @Operation(summary = "Update an existing item", description = "Updates the details of an existing item. Only the details provided in the request will be updated.")
    public ItemResponse update(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid UpdateItemRequest request) {
        UUID userId = userDetails.getUser().getId();

        var output = updateItemUseCase.execute(new UpdateItemUseCase.Input(
            request.id(),
            userId,
            request.name(),
            request.description(),
            request.acquisitionDate(),
            request.imageFilesUrls(),
            request.attributes(),
            request.tags()
        ));

       return new ItemResponse(
            output.id(),
            output.collectionId(),
            output.userId(),
            output.name(),
            output.description(),
            output.acquisitionDate(),
            output.lastUsedDate(),
            output.imageFilesUrls(),
            output.attributes(),
            output.likesCount(),
            output.commentsCount(),
            output.tags(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt()
        );
    }
}