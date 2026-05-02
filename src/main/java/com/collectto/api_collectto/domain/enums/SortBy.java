package com.collectto.api_collectto.domain.enums;

public enum SortBy {
    NAME_ASC("name", "ASC"),
    NAME_DESC("name", "DESC"),
    CREATED_AT_ASC("createdAt", "ASC"),
    CREATED_AT_DESC("createdAt", "DESC"),
    UPDATED_AT_ASC("updatedAt", "ASC"),
    UPDATED_AT_DESC("updatedAt", "DESC");

    private final String field;
    private final String direction;

    SortBy(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() { return field; }
    public String getDirection() { return direction; }
}
