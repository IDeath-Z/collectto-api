package com.collectto.api_collectto.domain.enums;

public enum Visibility {
    
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE"),
    FRIENDS("FRIENDS");

    private final String code;

    Visibility(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public static Visibility fromCode(String code) {
        for (Visibility visibility : Visibility.values()) {
            if (visibility.code.equals(code)) {
                return visibility;
            }
        }
        throw new IllegalArgumentException("Invalid visibility: " + code);
    }
}
