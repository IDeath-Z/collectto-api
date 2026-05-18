package com.collectto.api_collectto.domain.enums;

public enum SocialContext {

    USER("USER"),
    COLLECTION("COLLECTION"),
    ITEM("ITEM");

    private final String code;

    SocialContext(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SocialContext fromCode(String code) {
        for (SocialContext context : SocialContext.values()) {
            if (context.code.equals(code)) {
                return context;
            }
        }
        throw new IllegalArgumentException("Unknown SocialContext code: " + code);
    }
}
