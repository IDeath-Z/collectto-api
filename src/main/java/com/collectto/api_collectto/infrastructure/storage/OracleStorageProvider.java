package com.collectto.api_collectto.infrastructure.storage;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.collectto.api_collectto.domain.ports.StorageProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.StringPrivateKeySupplier;
import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.CreatePreauthenticatedRequestResponse;
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
        try {
            // Cleaning up the configuration values to ensure there are no extra quotes or whitespace
            String cleanRegion = region.replace("\"", "").trim();
            String cleanTenant = tenantId.replace("\"", "").trim();
            String cleanUser = userId.replace("\"", "").trim();
            String cleanFingerprint = fingerprint.replace("\"", "").trim();

            // The private key often has newlines and may be stored with escaped characters, so we need to handle that properly
            String finalPrivateKey = privateKey
                .replace("\"", "")
                .replace("\\n", "\n")
                .trim();

            SimpleAuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                .tenantId(cleanTenant)
                .userId(cleanUser)
                .fingerprint(cleanFingerprint)
                .privateKeySupplier(new StringPrivateKeySupplier(finalPrivateKey))
                .region(Region.valueOf(cleanRegion))
                .build();

            this.client = ObjectStorageClient.builder().build(provider);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Oracle Storage Client", e);
        }
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty())
            throw new IllegalArgumentException("Invalid file: The file name cannot be null or empty."); // Implement better error handling as needed

        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalName.replaceAll("\\s+", "_");
        String objectName;
        if (folder != null && !folder.trim().isEmpty()) {
            String cleanFolder = folder.replaceAll("^/+", "").replaceAll("/+$", "");
            objectName = cleanFolder + "/" + uniqueFileName;
        } else {
            objectName = uniqueFileName;
        }

        try {
            byte[] fileBytes = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);

            PutObjectRequest request = PutObjectRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .objectName(objectName)
                .putObjectBody(inputStream)
                .contentType(file.getContentType())
                .contentLength((long) fileBytes.length)
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
            
        } catch (BmcException e) {
            if (e.getStatusCode() == 404) {
                return;
            }
            throw new RuntimeException("Error from Oracle Cloud while deleting image: " + e.getMessage());
            
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while deleting image: " + e.getMessage());
        }
    }

    private String extractObjectNameFromUrl(String fullUrl) {
        if (fullUrl == null || !fullUrl.contains("/o/"))
            throw new IllegalArgumentException("Oracle URL is invalid: " + fullUrl); // Implement better error handling as needed

        String encodedObjectName = fullUrl.substring(fullUrl.lastIndexOf("/o/") + 3);
        return URLDecoder.decode(encodedObjectName, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public String generatePresignedUrl(String objectName, String contentType, int expirationMinutes) {
        try {
            CreatePreauthenticatedRequestDetails details = CreatePreauthenticatedRequestDetails.builder()
                .name("presigned-" + UUID.randomUUID())
                .objectName(objectName)
                .accessType(CreatePreauthenticatedRequestDetails.AccessType.ObjectWrite)
                .timeExpires(new Date(System.currentTimeMillis() + (long) expirationMinutes * 60 * 1000))
                .build();

            CreatePreauthenticatedRequestRequest request = CreatePreauthenticatedRequestRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .createPreauthenticatedRequestDetails(details)
                .build();

            CreatePreauthenticatedRequestResponse response = client.createPreauthenticatedRequest(request);
            String accessUri = response.getPreauthenticatedRequest().getAccessUri();

            return "https://objectstorage." + region + ".oraclecloud.com" + accessUri;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate pre-signed URL", e.getCause()); // Implement better error handling as needed
        }
    }

    @Override
    public String buildPublicUrl(String filePath) {
        return String.format(
            "https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
            region, namespaceName, bucketName, filePath);
    }
}
