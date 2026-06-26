package com.collectto.api_collectto.application.usecases.collection;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FetchUserCollectionsUseCase {

    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;

    public record Input(UUID userId, UUID requesterId, DomainPageRequest pageRequest) {}

    public record CollectionSummary(UUID id, String name, List<String> imagesURL) {}

    public record Output(List<CollectionSummary> collections, int totalPages, long totalElements, int currentPage) {}

    public Output execute(Input input) {
        DomainPageResult<Collection> pageableCollections = collectionRepository
            .findVisibleCollections(input.userId(), input.requesterId(), input.pageRequest());

        List<UUID> collectionIds = pageableCollections.content().stream()
            .map(Collection::getId)
            .toList();

        Map<UUID, List<String>> collectionImagesMap = collectionIds.isEmpty() 
            ? Map.of() 
            : itemRepository.findTop3MediaUrlsByCollectionIds(collectionIds);

        List<CollectionSummary> collections = pageableCollections.content().stream()
            .map(collection -> new CollectionSummary(
                collection.getId(),
                collection.getName(),
                collectionImagesMap.getOrDefault(collection.getId(), List.of())
            ))
            .toList();

        return new Output(
            collections,
            pageableCollections.totalPages(),
            pageableCollections.totalElements(),
            pageableCollections.page()
        );
    }
}