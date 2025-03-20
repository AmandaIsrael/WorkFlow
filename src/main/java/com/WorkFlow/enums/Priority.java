package com.WorkFlow.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Priority {

    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int code;

    Priority(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}