package com.collectto.api_collectto.domain.enums;

public enum UploadContext {

    PROFILE_PICTURE("PROFILE_PICTURE"),
    PROFILE_BACKGROUND("PROFILE_BACKGROUND"),
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
