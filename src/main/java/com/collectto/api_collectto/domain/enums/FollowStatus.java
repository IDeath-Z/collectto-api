package com.collectto.api_collectto.domain.enums;

public enum FollowStatus {

    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED");

    private final String code;

    FollowStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static FollowStatus fromCode(String code) {
        for (FollowStatus status : FollowStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown FollowStatus code: " + code); // Handle unknown code as needed
    }
}
