package com.collectto.api_collectto.application.usecases.item;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateItemUseCase {

    @Value("${storage.path.items}")
    private String itemsStoragePath;

    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;
    private final StorageProvider storageProvider;

    public record Input(UUID collectionId, UUID userId, String name, String description, String acquisitionDate,
            String lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags) {}
            
    public record Output(UUID id, UUID collectionId, UUID userId, String name, String description, String acquisitionDate,
            String lastUsedDate, List<String> imageFilesUrls, Map<String, Object> attributes, List<String> tags, boolean isActive, String createdAt, String updatedAt) {}
    
    public Output execute(Input input) {

        Collection collection = collectionRepository.findById(input.collectionId())
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        if (!collection.getUserId().equals(input.userId()))
            throw new RuntimeException("User is not the owner of the collection");

        UUID itemId = UUID.randomUUID();
        List<String> imageFilesUrls = (input.imageFilesUrls() == null || input.imageFilesUrls().isEmpty())
                        ? null
                        : input.imageFilesUrls().stream()
                                        .filter(path -> {
                                                if (!path.startsWith("items/"))
                                                        throw new RuntimeException("Invalid image path"); // Implement better validation as needed
                                                return true;
                                        })
                                        .map(storageProvider::buildPublicUrl)
                                        .toList();

        Item item = new Item(
                itemId,
                input.collectionId(),
                input.userId(),
                input.name(),
                input.description(),
                LocalDate.parse(input.acquisitionDate()),
                LocalDate.parse(input.lastUsedDate()),
                imageFilesUrls,
                input.attributes(),
                0,
                0,
                input.tags(),
                true,
                Instant.now(),
                Instant.now());

        Item savedItem = itemRepository.save(item);

        return new Output(
                savedItem.getId(),
                savedItem.getCollectionId(),
                savedItem.getUserId(),
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getAcquisitionDate().toString(),
                savedItem.getLastUsedDate().toString(),
                savedItem.getMediaURLs(),
                savedItem.getAttributes(),
                savedItem.getTags(),
                savedItem.isActive(),
                savedItem.getCreatedAt().toString(),
                savedItem.getUpdatedAt().toString()
        );
    }
}
