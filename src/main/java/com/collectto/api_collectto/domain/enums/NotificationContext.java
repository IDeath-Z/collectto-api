package com.collectto.api_collectto.domain.enums;

public enum NotificationContext {

    USER_FOLLOWED("USER_FOLLOWED"),
    COLLECTION_FOLLOWED("COLLECTION_FOLLOWED"),
    ITEM_COMMENTED("ITEM_COMMENTED"),
    ITEM_LIKED("ITEM_LIKED");

    private final String code;

    NotificationContext(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NotificationContext fromCode(String code) {
        for (NotificationContext notification : NotificationContext.values()) {
            if (notification.code.equals(code)) {
                return notification;
            }
        }
        throw new IllegalArgumentException("Unknown NotificationContext code: " + code);
    }
}
