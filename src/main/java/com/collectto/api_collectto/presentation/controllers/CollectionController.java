package com.collectto.api_collectto.presentation.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.collection.CreateCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collection.FetchCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collection.FetchUserCollectionsUseCase;
import com.collectto.api_collectto.domain.enums.SortBy;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.infrastructure.security.SecurityUserDetails;
import com.collectto.api_collectto.presentation.dto.collection.CreateCollectionRequest;
import com.collectto.api_collectto.presentation.dto.collection.CollectionPageResponse;
import com.collectto.api_collectto.presentation.dto.collection.CollectionResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectionController {

    private final CreateCollectionUseCase createCollectionUseCase;
    private final FetchCollectionUseCase fetchCollectionUseCase;
    private final FetchUserCollectionsUseCase fetchUserCollectionsUseCase;

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

@GetMapping("{collectionId}")
    public CollectionResponse getCollection(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID collectionId) {
        UUID requesterId = userDetails.getUser().getId();

        var output = fetchCollectionUseCase.execute(new FetchCollectionUseCase.Input(collectionId, requesterId));

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
    
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Fetch paginated collections of a user", description = "Retrieves a paginated list of collections for a specific user, with visibility filtering based on the requester's relationship to the collection owner.")
    public CollectionPageResponse getPaginatedCollections(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "CREATED_AT_DESC") SortBy sortBy) {
            
        UUID requesterId = userDetails.getUser().getId();
            
        var output = fetchUserCollectionsUseCase.execute(new FetchUserCollectionsUseCase.Input(
                userId,
                requesterId,
                new DomainPageRequest(page, size, sortBy)
        ));
        
        return new CollectionPageResponse(
                output.collections().stream()
                    .map(collection -> new CollectionPageResponse.CollectionSummaryResponse(
                            collection.id(),
                            collection.name(),
                            collection.imagesURL()
                    ))
                    .toList(),
                output.totalPages(),
                output.totalElements(),
                output.currentPage()
        );
    }
}
