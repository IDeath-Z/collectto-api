package com.collectto.api_collectto.application.usecases.itemcomment;

import java.util.UUID;

import com.collectto.api_collectto.application.exceptions.BusinessRuleException;
import com.collectto.api_collectto.application.exceptions.ResourceNotFoundException;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.ItemComment;
import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.ports.ItemCommentRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteCommentUseCase {

    private final ItemCommentRepository itemCommentRepository;
    private final ItemRepository itemRepository;
    private final NotificationRepository notificationRepository;

    public record Input(UUID commentId, UUID userId) {
    }

    public void execute(Input input) {
        ItemComment comment = itemCommentRepository.findById(input.commentId())
        .orElseThrow(( ) -> new ResourceNotFoundException("Comment not found with ID " + input.commentId()));

        Item item = itemRepository.findById(comment.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID " + comment.getItemId()));

        if (!comment.getAuthorId().equals(input.userId()) && !item.getUserId().equals(input.userId()))
            throw new BusinessRuleException("User is not the author of the comment or the owner of the item");

        itemCommentRepository.deleteById(comment.getId());
        itemRepository.decrementCommentsCount(comment.getItemId());
        notificationRepository.deleteByActorIdAndReferenceIdAndContext(
            comment.getAuthorId(), 
            comment.getId(), 
            NotificationContext.ITEM_COMMENTED
        );
    }
}
