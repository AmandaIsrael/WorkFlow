package com.WorkFlow.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    PENDING(0),
    IN_PROGRESS(1),
    COMPLETED(2),
    BLOCKED(3),
    CANCELED(4),
    LATE(5);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}