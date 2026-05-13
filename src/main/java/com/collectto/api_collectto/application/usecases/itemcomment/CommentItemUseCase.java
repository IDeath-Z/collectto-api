package com.collectto.api_collectto.application.usecases.itemcomment;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.ItemComment;
import com.collectto.api_collectto.domain.enums.Visibility;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemCommentRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentItemUseCase {

    private final ItemCommentRepository itemCommentRepository;
    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;

    public record Input(UUID itemId, UUID authorId, String content) {}
    public record Output(UUID commentId, UUID itemId, UUID authorId, String content, String createdAt) {}

    public Output execute(Input input) {
        Item item = itemRepository.findById(input.itemId())
            .orElseThrow(() -> new RuntimeException("Item not found"));

        Collection collection = collectionRepository.findById(item.getCollectionId())
            .orElseThrow(() -> new RuntimeException("Collection not found"));

        if (!collection.getUserId().equals(input.authorId()) && collection.getVisibility() == Visibility.PRIVATE)
            throw new RuntimeException("Unauthorized access to private collection"); // Implement better exception handling as needed

        UUID commentId = UUID.randomUUID();

        ItemComment comment = new ItemComment(
            commentId,
            input.itemId(),
            input.authorId(),
            input.content(),
            Instant.now()
        );

        ItemComment savedComment = itemCommentRepository.save(comment);
        itemRepository.incrementCommentsCount(savedComment.getItemId());

        return new Output(
            savedComment.getId(),
            savedComment.getItemId(),
            savedComment.getAuthorId(),
            savedComment.getContent(),
            savedComment.getCreatedAt().toString()
        );
    }
}
