package com.collectto.api_collectto.application.usecases.notification;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.entities.Item;
import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.ports.CollectionRepository;
import com.collectto.api_collectto.domain.ports.ItemRepository;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CollectionRepository collectionRepository;

    public record Input(UUID recipientId, DomainPageRequest pageRequest) {}
    public record ActorSummary(UUID id, String username, String profilePictureUrl) {}
    public record ReferenceSummary(UUID id, UUID parentId, String referenceImageUrl) {}
    public record NotificationSummary(UUID id, UUID recipientId, ActorSummary actor, NotificationContext context, ReferenceSummary reference, boolean isRead, String createdAt) {}
    public record Output(List<NotificationSummary> notifications, int totalPages, long totalElements, int currentPage) {}

    public Output execute(Input input) {
        DomainPageResult<Notification> notificationPage = notificationRepository.findByRecipientId(input.recipientId(), input.pageRequest());

        List<UUID> actorIds = notificationPage.content().stream()
            .map(Notification::getActorId)
            .toList();

        Map<UUID, User> userMap = userRepository.findAllByIds(actorIds).stream()
            .collect(Collectors.toMap(User::getId, user -> user));

        List<UUID> itemIds = notificationPage.content().stream()
            .filter(notification -> notification.getContext() == NotificationContext.ITEM_LIKED || notification.getContext() == NotificationContext.ITEM_COMMENTED)
            .map(Notification::getReferenceId)
            .toList();

        Map<UUID, Item> itemMap = itemIds.isEmpty() ? Map.of() : 
            itemRepository.findAllByIds(itemIds).stream().collect(Collectors.toMap(Item::getId, item -> item));

        List<UUID> collectionIds = notificationPage.content().stream()
            .filter(notification -> notification.getContext() == NotificationContext.COLLECTION_FOLLOWED)
            .map(Notification::getReferenceId)
            .toList();

        Map<UUID, Collection> collectionMap = collectionIds.isEmpty() ? Map.of() : 
            collectionRepository.findAllByIds(collectionIds).stream().collect(Collectors.toMap(Collection::getId, collection -> collection));

        List<NotificationSummary> notifications = buildSummaryList(notificationPage, userMap, itemMap, collectionMap);

        return new Output(
            notifications,
            notificationPage.totalPages(),
            notificationPage.totalElements(),
            notificationPage.page()
        );
    }

    private List<NotificationSummary> buildSummaryList(DomainPageResult<Notification> notificationPage, Map<UUID, User> userMap, 
        Map<UUID, Item> itemMap, Map<UUID, Collection> collectionMap) {
        return notificationPage.content().stream()
            .filter(notification -> userMap.containsKey(notification.getActorId()))
            .map(notification -> {
                User actor = userMap.get(notification.getActorId());
                ActorSummary actorSummary = new ActorSummary(
                    actor.getId(), actor.getUsername(), actor.getProfilePictureUrl()
                );

                ReferenceSummary referenceSummary = null;

                switch (notification.getContext()) {
                    case ITEM_LIKED, ITEM_COMMENTED -> {
                        Item item = itemMap.get(notification.getReferenceId());
                        if (item != null) {
                            referenceSummary = new ReferenceSummary(
                                item.getId(),
                                item.getCollectionId(),
                                item.getMediaURLs().getFirst()
                            );
                        }
                    }
                    case COLLECTION_FOLLOWED -> {
                        Collection collection = collectionMap.get(notification.getReferenceId());
                        if (collection != null) {
                            referenceSummary = new ReferenceSummary(
                                collection.getId(),
                                null, // Collections don't have a parent entity
                                collection.getCoverImageUrl()
                            );
                        }
                    }
                    case USER_FOLLOW_REQUESTED, USER_ACCEPTED_FOLLOW_REQUEST -> {
                        referenceSummary = null;
                    }
                }

                return new NotificationSummary(
                    notification.getId(), notification.getRecipientId(), actorSummary, 
                    notification.getContext(), referenceSummary, notification.isRead(), 
                    notification.getCreatedAt().toString()
                );
            })
            .toList();
    }
}
