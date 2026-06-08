package com.collectto.api_collectto.domain.enums;

public enum TokenType {
    
    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    private final String code;

    TokenType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public static TokenType fromCode(String code) {
        for (TokenType tokenType : TokenType.values()) {
            if (tokenType.code.equals(code)) {
                return tokenType;
            }
        }
        throw new IllegalArgumentException("Invalid token type: " + code);
    }
}
