package com.collectto.api_collectto.infrastructure.jobs;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.user.UserJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.collection.CollectionJpaRepository;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaEntity;
import com.collectto.api_collectto.infrastructure.persistence.item.ItemJpaRepository;
import com.collectto.api_collectto.domain.ports.StorageProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataPurgeScheduler {

    private final UserJpaRepository userRepository;
    private final CollectionJpaRepository collectionRepository;
    private final ItemJpaRepository itemRepository;
    private final StorageProvider storageProvider;

    @Value("${collectto.purge-retention-days:30}")
    private int retentionDays;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void purgeExpiredData() {
        Instant cutoffDate = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        log.info("[CRON] Starting data purge (Cutoff Date: {})...", cutoffDate);
        
        List<ItemJpaEntity> itemsToPurge = itemRepository.findItemsToPurge(cutoffDate);
        for (ItemJpaEntity item : itemsToPurge) {
            List<String> mediaUrls = item.getMediaUrls();
            if (mediaUrls != null) {
                for (String url : mediaUrls) {
                    safeDeleteImage(url);
                }
            }
        }
        int itemsDeleted = itemRepository.purgeOldDeactivatedItems(cutoffDate);

        List<CollectionJpaEntity> collectionsToPurge = collectionRepository.findCollectionsToPurge(cutoffDate);
        for (CollectionJpaEntity collection : collectionsToPurge) {
            safeDeleteImage(collection.getCoverImageUrl());
        }
        int collectionsDeleted = collectionRepository.purgeOldDeactivatedCollections(cutoffDate);

        List<UserJpaEntity> usersToPurge = userRepository.findUsersToPurge(cutoffDate);
        for (UserJpaEntity user : usersToPurge) {
            safeDeleteImage(user.getProfilePictureUrl());
            safeDeleteImage(user.getProfileBackgroundUrl());
        }
        int usersDeleted = userRepository.purgeOldDeactivatedUsers(cutoffDate);
        
        log.info("[CRON] Data purge completed! Removed: {} Users, {} Collections, {} Items.", 
                 usersDeleted, collectionsDeleted, itemsDeleted);
    }

    private void safeDeleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }
        
        try {
            storageProvider.deleteImage(imageUrl);
        } catch (Exception e) {
            log.error("[CRON] Failed to delete image from Oracle Cloud: {}", imageUrl, e);
        }
    }
}