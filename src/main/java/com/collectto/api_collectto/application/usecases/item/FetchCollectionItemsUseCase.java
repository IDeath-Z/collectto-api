package com.collectto.api_collectto.application.usecases.item;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.ForbiddenActionException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.UserFollowRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchCollectionItemsUseCase {

    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;
    private final UserFollowRepository userFollowRepository;

    public record Input(UUID collectionId, UUID requesterId, DomainPageRequest pageRequest) {}

    public record ItemSummary(UUID id, String name, List<String> imagesURL) {}

    public record Output(List<ItemSummary> items, int totalPages, long totalItems, int currentPage) {}

    public Output execute(Input input) {
        Collection collection = collectionRepository.findById(input.collectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + input.collectionId()));

        if (!collection.getUserId().equals(input.requesterId())) {
            if (collection.getVisibility() == Visibility.PRIVATE)
                throw new ForbiddenActionException("User does not have permission to access items of this collection");

            if (collection.getVisibility() == Visibility.FRIENDS)
                if (!userFollowRepository.isFollowing(input.requesterId(), collection.getUserId()))
                    throw new ForbiddenActionException("User does not have permission to access items of this collection");
        }

        DomainPageResult<Item> pageableItems = itemRepository.findByCollectionId(input.collectionId(), input.pageRequest());

        List<ItemSummary> items = pageableItems.content().stream()
            .map(item -> new ItemSummary(item.getId(), item.getName(), 
                item.getMediaURLs() == null ? List.of() : item.getMediaURLs().stream().limit(3).toList())) // Limit to 3 images for summary, which 3? i don't know, maybe the first 3? Fix as needed
            .toList();

        return new Output(items, pageableItems.totalPages(), pageableItems.totalElements(), pageableItems.page());
    }
}
