package com.collectto.api_collectto.presentation.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateCommentRequest", description = "Payload for creating a new comment on an item")
public record CreateCommentRequest(
    @NotBlank @Schema(description = "The message of the comment")
    String content
) {}
