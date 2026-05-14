package com.collectto.api_collectto.application.usecases.collection;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchUserCollectionsUseCase {

    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;

    public record Input(UUID userId, UUID requesterId, DomainPageRequest pageRequest) {}

    public record CollectionSummary(UUID id, String name, List<String> imagesURL) {}

    public record Output(List<CollectionSummary> collections, int totalPages, long totalElements, int currentPage) {}

    public Output execute(Input input) {
        DomainPageResult<Collection> pageableCollections = collectionRepository.findByUserId(input.userId(), input.pageRequest());

        // TODO: Fix N+1 problem here, put a findAllByIds

        List<CollectionSummary> collections = pageableCollections.content().stream()    
            .filter(collection -> {
                if (collection.getUserId().equals(input.requesterId())) return true;
                return collection.getVisibility() != Visibility.PRIVATE; // Implements FRIENDS visibility later
            })
            .map(collection -> new CollectionSummary(
                collection.getId(),
                collection.getName(),
                itemRepository.findTop3MediaUrlsByCollectionId(collection.getId())
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
