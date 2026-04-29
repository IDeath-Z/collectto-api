package com.collectto.api_collectto.presentation.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.collectto.api_collectto.application.usecases.storage.GenerateUploadUrlsUseCase;
import com.collectto.api_collectto.infrastructure.security.SecurityUserDetails;
import com.collectto.api_collectto.presentation.dto.storage.GenerateUploadUrlsRequest;
import com.collectto.api_collectto.presentation.dto.storage.GenerateUploadUrlsResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/uploads")
public class UploadController {

    private final GenerateUploadUrlsUseCase generateUploadUrlsUseCase;

    @PostMapping("/presigned-urls")
    @Operation(summary = "Generate pre-signed upload URLs", description = "Returns pre-signed URLs for direct upload to storage. Valid for 5 minutes.")
    public GenerateUploadUrlsResponse generatePresignedUrls(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid GenerateUploadUrlsRequest request) {
        UUID userId = userDetails.getUser().getId();

        var output = generateUploadUrlsUseCase.execute(new GenerateUploadUrlsUseCase.Input(
                userId,
                request.resourceId(),
                request.parentId(),
                request.context(),
                request.files().stream()
                        .map(f -> new GenerateUploadUrlsUseCase.FileInput(f.fileName(), f.contentType()))
                        .toList()));

        return new GenerateUploadUrlsResponse(
                output.resourceId(),
                output.files().stream()
                        .map(f -> new GenerateUploadUrlsResponse.FileOutput(f.filePath(), f.uploadUrl()))
                        .toList()); // Implements response entity later
    }
}
