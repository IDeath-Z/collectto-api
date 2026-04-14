package com.collectto.api_collectto.infrastructure.storage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.StringPrivateKeySupplier;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.Region;

import jakarta.annotation.PostConstruct;

@Component
public class OracleStorageProvider implements StorageProvider {

    @Value("${oracle.cloud.tenantId}")
    private String tenantId;

    @Value("${oracle.cloud.userId}")
    private String userId;

    @Value("${oracle.cloud.fingerprint}")
    private String fingerprint;

    @Value("${oracle.cloud.region}")
    private String region;

    @Value("${oracle.cloud.namespace}")
    private String namespaceName;

    @Value("${oracle.cloud.bucket}")
    private String bucketName;

    @Value("${oracle.cloud.privateKey}")
    private String privateKey;

    private ObjectStorage client;

    @PostConstruct
    public void init() {
        String finalPrivateKey = privateKey.replace("\\n", "\n");

        SimpleAuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                .tenantId(tenantId)
                .userId(userId)
                .fingerprint(fingerprint)
                .privateKeySupplier(new StringPrivateKeySupplier(finalPrivateKey))
                .region(Region.valueOf(region))
                .build();
        
        this.client = ObjectStorageClient.builder().build(provider);
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) {

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file: The file name cannot be null or empty."); // Implement better error handling as needed
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalName.replaceAll("\\s+", "_");
        String objectName;
        if (folder != null && !folder.trim().isEmpty()) {
            String cleanFolder = folder.replaceAll("^/+", "").replaceAll("/+$", "");
            objectName = cleanFolder + "/" + uniqueFileName;
        } else {
            objectName = uniqueFileName;
        }

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .namespaceName(namespaceName)
                    .bucketName(bucketName)
                    .objectName(objectName)
                    .putObjectBody(file.getInputStream())
                    .contentType(file.getContentType())
                    .build();

            client.putObject(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Oracle Object Storage", e); // Implement better error handling as needed
        }

        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8).replace("%2F", "/");
        return String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                region, namespaceName, bucketName, encodedObjectName);
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .namespaceName(namespaceName)
                    .bucketName(bucketName)
                    .objectName(extractObjectNameFromUrl(imageUrl))
                    .build();

            client.deleteObject(request);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting image: " + e.getMessage()); // Implement better error handling as needed
        }
    }

    private String extractObjectNameFromUrl(String fullUrl) {
        if (fullUrl == null || !fullUrl.contains("/o/")) {
            throw new IllegalArgumentException("Oracle URL is invalid: " + fullUrl); // Implement better error handling as needed
        }

        String encodedObjectName = fullUrl.substring(fullUrl.lastIndexOf("/o/") + 3);
        return java.net.URLDecoder.decode(encodedObjectName, java.nio.charset.StandardCharsets.UTF_8);
    }
}
