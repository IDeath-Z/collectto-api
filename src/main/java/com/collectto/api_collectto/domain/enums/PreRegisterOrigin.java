package com.collectto.api_collectto.domain.enums;

public enum PreRegisterOrigin {

    MOBI2026("mobi2026"),
    SOCIAL_MEDIA("social_media");

    private final String code;

    PreRegisterOrigin(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PreRegisterOrigin fromCode(String code) {
        for (PreRegisterOrigin origin : PreRegisterOrigin.values()) {
            if (origin.code.equals(code)) {
                return origin;
            }
        }
        throw new IllegalArgumentException("Unknown PreRegisterOrigin code: " + code);
    }
}
