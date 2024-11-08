package com.maimai.nlsearch.common.enums;

public enum ShippingType {
    EXPRESS("express"), STANDARD("standard"), PICKUP("pickup"), OVERNIGHT("overnight");

    private final String value;

    ShippingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static ShippingType fromString(String value) {
        for (ShippingType type: ShippingType.values()) {
            if (type.value.equalsIgnoreCase(value)) return type;
        }
        throw new IllegalArgumentException("Unknown shipping type: " + value);
    }
}
