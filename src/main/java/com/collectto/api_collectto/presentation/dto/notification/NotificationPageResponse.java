package com.collectto.api_collectto.presentation.dto.notification;

import java.util.List;
import java.util.UUID;

import com.collectto.api_collectto.domain.enums.NotificationContext;

import io.swagger.v3.oas.annotations.media.Schema;

public record NotificationPageResponse(
    @Schema(description = "List of notifications for the user")
    List<NotificationSummary> notifications,

    @Schema(description = "Total number of pages")
    int totalPages,

    @Schema(description = "Total number of elements")
    long totalElements,

    @Schema(description = "Current page number")
    int currentPage
) {
    public record NotificationSummary(
        @Schema(description = "Unique identifier of the notification", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID notificationId,

        @Schema(description = "Unique identifier of the recipient", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID recipientId,

        @Schema(description = "Summary of the actor associated with the notification")
        ActorSummary actor,

        @Schema(description = "Context of the notification")
        NotificationContext context,

        @Schema(description = "Summary of the reference entity associated with the notification")
        ReferenceSummary reference,

        @Schema(description = "Indicates whether the notification has been read")
        boolean read,

        @Schema(description = "Timestamp of when the notification was created", example = "2026-06-01T12:34:56Z")
        String createdAt
    ) {}

    public record ActorSummary(
        @Schema(description = "Unique identifier of the actor", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Username of the actor")
        String username,

        @Schema(description = "URL of the actor's profile picture", example = "https://...")
        String profilePictureUrl
    ) {}

    public record ReferenceSummary(
        @Schema(description = "Unique identifier of the reference entity, depending on the context", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Unique identifier of the parent entity, case is a collection return null", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID parentId,

        @Schema(description = "URL of the reference image", example = "https://...")
        String referenceImageUrl
    ) {}
}