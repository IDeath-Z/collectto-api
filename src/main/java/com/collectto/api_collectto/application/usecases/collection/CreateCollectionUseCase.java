package com.collectto.api_collectto.application.usecases.collection;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateCollectionUseCase {

    private final CollectionRepository collectionsRepository;
    private final StorageProvider storageProvider;

    public record Input(UUID userId, String name, String description, MultipartFile coverImage, String folder, List<String> tags) {}
    public record Output(UUID id, UUID userId, String name, String description, String coverImageURL, Visibility visibility,
            int followersCount, List<String> tags, boolean isActive, String createdAt, String updatedAt) {
    }
                
    public Output execute(Input input) {

        String imageUrl;
        if (input.coverImage() == null || input.coverImage().isEmpty()) {
            imageUrl = null;
        } else {
            imageUrl = storageProvider.uploadImage(input.coverImage(), input.folder());
        }

        Collection collection = new Collection(
                UUID.randomUUID(),
                input.userId(),
                input.name(),
                input.description(),
                imageUrl,
                Visibility.PRIVATE, // Default visibility, can be changed later
                0,
                input.tags(),
                true,
                Instant.now(),
                Instant.now());

        Collection savedCollection = collectionsRepository.save(collection);

        return new Output(
                savedCollection.getId(),
                savedCollection.getUserId(),
                savedCollection.getName(),
                savedCollection.getDescription(),
                savedCollection.getCoverImageUrl(),
                savedCollection.getVisibility(),
                savedCollection.getFollowersCount(),
                savedCollection.getTags(),
                savedCollection.isActive(),
                savedCollection.getCreatedAt().toString(),
                savedCollection.getUpdatedAt().toString());
    }
}
