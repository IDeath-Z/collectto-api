package com.collectto.api_collectto.presentation.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.collectto.api_collectto.application.usecases.collection.CreateCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collection.DeleteCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collection.FetchCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collection.FetchUserCollectionsUseCase;
import com.collectto.api_collectto.application.usecases.collection.UpdateCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collectionfollow.FollowCollectionUseCase;
import com.collectto.api_collectto.application.usecases.collectionfollow.UnfollowCollectionUseCase;
import com.collectto.api_collectto.domain.enums.SortBy;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.infrastructure.persistence.shared.TransactionalProxy;
import com.collectto.api_collectto.infrastructure.security.SecurityUserDetails;
import com.collectto.api_collectto.presentation.dto.collection.CreateCollectionRequest;
import com.collectto.api_collectto.presentation.dto.collection.UpdateCollectionRequest;
import com.collectto.api_collectto.presentation.dto.collection.CollectionFollowResponse;
import com.collectto.api_collectto.presentation.dto.collection.CollectionPageResponse;
import com.collectto.api_collectto.presentation.dto.collection.CollectionResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectionController {

    private final CreateCollectionUseCase createCollectionUseCase;
    private final DeleteCollectionUseCase deleteCollectionUseCase;
    private final FetchCollectionUseCase fetchCollectionUseCase;
    private final FetchUserCollectionsUseCase fetchUserCollectionsUseCase;
    private final UpdateCollectionUseCase updateCollectionUseCase;
    private final FollowCollectionUseCase followCollectionUseCase;
    private final UnfollowCollectionUseCase unfollowCollectionUseCase;
    private final TransactionalProxy transactionalProxy;

    @PostMapping(value = "/create")
    @Operation(summary = "Create a new collection", description = "Registers a new collection in the system with the provided details.")
    public CollectionResponse create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateCollectionRequest request) {
        UUID userId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createCollectionUseCase.execute(
            new CreateCollectionUseCase.Input(
                userId,
                request.name(),
                request.description(),
                request.coverImageUrl(),
                request.tags()
            )
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

    @DeleteMapping("{collectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a collection", description = "Deletes a collection by its ID. Only the owner of the collection can perform this action.")
    public void deleteCollection(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID collectionId) {
        UUID userId = userDetails.getUser().getId();

        transactionalProxy.execute(() -> deleteCollectionUseCase.execute(
            new DeleteCollectionUseCase.Input(collectionId, userId)
        ));
    }

    @GetMapping("{collectionId}")
    @Operation(summary = "Fetch collection details", description = "Retrieves the details of a specific collection by its ID, with visibility filtering based on the requester's relationship to the collection owner.")
    public CollectionResponse getCollection(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID collectionId) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.executeReadOnly(() -> fetchCollectionUseCase.execute(
            new FetchCollectionUseCase.Input(collectionId, requesterId)
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
    
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Fetch paginated collections of a user", description = "Retrieves a paginated list of collections for a specific user, with visibility filtering based on the requester's relationship to the collection owner.")
    public CollectionPageResponse getPaginatedCollections(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "CREATED_AT_DESC") SortBy sortBy) {
            
        UUID requesterId = userDetails.getUser().getId();
            
        var output = transactionalProxy.executeReadOnly(() -> fetchUserCollectionsUseCase.execute(
            new FetchUserCollectionsUseCase.Input(
                userId,
                requesterId,
                new DomainPageRequest(page, size, sortBy)
            )
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

    @PatchMapping("/update")
    @Operation(summary = "Update an existing collection", description = "Updates the details of an existing collection. Only the fields provided in the request will be updated.")
    public CollectionResponse patchCollection(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid UpdateCollectionRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateCollectionUseCase.execute(
            new UpdateCollectionUseCase.Input(
                request.id(),
                requesterId,
                request.name(),
                request.description(),
                request.coverImageUrl(),
                request.visibility(),
                request.tags()
            )
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

    // Follow
    @PostMapping("/follow/{collectionId}")
    @Operation(summary = "Follow a collection", description = "Allows the authenticated user to follow a collection, increasing its followers count and enabling it to appear in the user's followed collections list.")
    public CollectionFollowResponse followCollection(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID collectionId) {
        UUID userId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> followCollectionUseCase.execute(
            new FollowCollectionUseCase.Input(userId, collectionId)
        ));
        
        return new CollectionFollowResponse(
            output.followerId(),
            output.collectionId(),
            output.createdAt()
        );
    }

    @DeleteMapping("/follow/{collectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Unfollow a collection", description = "Allows the authenticated user to unfollow a collection, decreasing its followers count")
    public void unfollowCollection(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID collectionId) {
        UUID userId = userDetails.getUser().getId();

        transactionalProxy.execute(() -> unfollowCollectionUseCase.execute(
            new UnfollowCollectionUseCase.Input(userId, collectionId)       
        ));
    }
}
