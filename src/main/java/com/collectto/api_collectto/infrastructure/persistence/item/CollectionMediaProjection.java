package com.collectto.api_collectto.infrastructure.persistence.item;

import java.util.List;
import java.util.UUID;

public interface CollectionMediaProjection {
    UUID getCollectionId();
    List<String> getMediaUrls();
}
