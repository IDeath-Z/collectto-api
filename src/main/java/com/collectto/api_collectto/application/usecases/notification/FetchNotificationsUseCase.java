package com.collectto.api_collectto.application.usecases.notification;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.collectto.api_collectto.domain.entities.Notification;
import com.collectto.api_collectto.domain.entities.User;
import com.collectto.api_collectto.domain.enums.NotificationContext;
import com.collectto.api_collectto.domain.ports.NotificationRepository;
import com.collectto.api_collectto.domain.ports.UserRepository;
import com.collectto.api_collectto.domain.shared.DomainPageRequest;
import com.collectto.api_collectto.domain.shared.DomainPageResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public record Input(UUID recipientId, DomainPageRequest pageRequest) {}
    public record ActorSummary(UUID id, String username, String profilePictureUrl) {}
    public record NotificationSummary(UUID id, UUID recipientId, ActorSummary actor, NotificationContext context, UUID referenceId, boolean isRead, String createdAt) {}
    public record Output(List<NotificationSummary> notifications, int totalPages, long totalElements, int currentPage) {}

    public Output execute(Input input) {
        DomainPageResult<Notification> notificationPage = notificationRepository.findByRecipientId(input.recipientId(), input.pageRequest());

        List<UUID> actorIds = notificationPage.content().stream()
            .map(Notification::getActorId)
            .toList();

        List<User> users = userRepository.findAllByIds(actorIds);

        Map<UUID, User> userMap = users.stream()
            .collect(Collectors.toMap(User::getId, user -> user));

        List<NotificationSummary> notifications = notificationPage.content().stream()
            .filter(notification -> userMap.containsKey(notification.getActorId()))
            .map(notification -> {
                User actor = userMap.get(notification.getActorId());
                ActorSummary actorSummary = new ActorSummary(
                    actor.getId(),
                    actor.getUsername(),
                    actor.getProfilePictureUrl()
                );

                return new NotificationSummary(
                    notification.getId(),
                    notification.getRecipientId(),
                    actorSummary,
                    notification.getContext(),
                    notification.getReferenceId(),
                    notification.isRead(),
                    notification.getCreatedAt().toString()
                );
            })
            .toList();

        return new Output(
            notifications,
            notificationPage.totalPages(),
            notificationPage.totalElements(),
            notificationPage.page()
        );
    }
}
