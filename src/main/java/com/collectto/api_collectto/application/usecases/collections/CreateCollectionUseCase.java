package com.collectto.api_collectto.application.usecases.collections;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.collectto.api_collectto.domain.entities.Collections;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionsRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;

@Service
public class CreateCollectionUseCase {

    @Autowired
    private CollectionsRepository collectionsRepository;

    @Autowired
    private StorageProvider storageProvider;

    public record Input(UUID userId, String name, String description, MultipartFile coverImage, String folder) {}
    public record Output(UUID id, UUID userId, String name, String description, String coverImageURL, Visibility visibility,
            int followersCount, boolean isActive, String createdAt, String updatedAt) {
    }
                
    public Output execute(Input input) {

        String imageUrl;
        if (input.coverImage() == null || input.coverImage().isEmpty()) {
            imageUrl = null;
        } else {
            imageUrl = storageProvider.uploadImage(input.coverImage(), input.folder());
        }

        Collections collection = new Collections(
                UUID.randomUUID(),
                input.userId(),
                input.name(),
                input.description(),
                imageUrl,
                Visibility.PRIVATE, // Default visibility, can be changed later
                0,
                true,
                Instant.now(),
                Instant.now());

        Collections savedCollection = collectionsRepository.save(collection);

        return new Output(
                savedCollection.getId(),
                savedCollection.getUserId(),
                savedCollection.getName(),
                savedCollection.getDescription(),
                savedCollection.getCoverImageUrl(),
                savedCollection.getVisibility(),
                savedCollection.getFollowersCount(),
                savedCollection.isActive(),
                savedCollection.getCreatedAt().toString(),
                savedCollection.getUpdatedAt().toString());
    }
}
