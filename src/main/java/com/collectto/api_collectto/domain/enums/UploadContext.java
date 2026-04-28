package com.collectto.api_collectto.domain.enums;

public enum UploadContext {
    USER_AVATAR("USER_AVATAR"),
    USER_BANNER("USER_BANNER"),
    COLLECTION("COLLECTION"),
    ITEM("ITEM");

    private final String code;

    UploadContext(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static UploadContext fromCode(String code) {
        for (UploadContext context : UploadContext.values()) {
            if (context.code.equals(code)) {
                return context;
            }
        }
        throw new IllegalArgumentException("Invalid upload context: " + code);
    }
}
