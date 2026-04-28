package com.collectto.api_collectto.domain.ports;

import org.springframework.web.multipart.MultipartFile;

public interface StorageProvider {
    String uploadImage(MultipartFile file, String folder);
    void deleteImage(String imageUrl);
    String generatePresignedUrl(String objectName, String contentType, int expirationMinutes);
    String buildPublicUrl(String filePath);
}
