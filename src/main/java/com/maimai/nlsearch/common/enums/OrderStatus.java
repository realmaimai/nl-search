package com.maimai.nlsearch.common.enums;

public enum OrderStatus {
    DELIVERED("delivered"),
    PROCESSING("processing"),
    SHIPPED("shipped"),
    PENDING("pending"),
    CANCELLED("cancelled"),
    ;

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static OrderStatus fromString(String str) {
        for (OrderStatus status: OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(str)) return status;
        }
        throw new IllegalArgumentException("Unknown order status: " + str);
    }
}
