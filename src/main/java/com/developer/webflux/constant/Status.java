package com.developer.webflux.constant;

public enum Status {
    ACTIVE(1), INACTIVE(0);

    private Integer value;

    Status(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
