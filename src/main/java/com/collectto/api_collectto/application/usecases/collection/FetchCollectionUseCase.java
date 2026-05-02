package com.collectto.api_collectto.application.usecases.collection;

import java.util.List;

import com.collectto.api_collectto.domain.ports.CollectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchCollectionUseCase {
    
    private final CollectionRepository collectionRepository;

        public record Input(String collectionId) {}

    public record Output(String id, String userId, String name, String description, String coverImageURL, 
        String visibility, int followersCount, List<String> tags, boolean isActive, String createdAt, String updatedAt) {}

    public Output execute(Input input) {

        return null;
    }
}
