package com.collectto.api_collectto.domain.shared;

public record StorageUrlPaths(
    String profilePicture,
    String profileBackground,
    String collectionsPath,
    String itemsPath
) {
    public boolean isProfilePictureValid(String url) {
        return url != null && url.startsWith(this.profilePicture + "/");
    }

    public boolean isProfileBackgroundValid(String url) {
        return url != null && url.startsWith(this.profileBackground + "/");
    }

    public boolean isCollectionPathValid(String url) {
        return url != null && url.startsWith(this.collectionsPath + "/");
    }

    public boolean isItemPathValid(String url) {
        return url != null && url.startsWith(this.itemsPath + "/");
    }
}
