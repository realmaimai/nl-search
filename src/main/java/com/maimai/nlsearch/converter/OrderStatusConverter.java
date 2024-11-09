package com.maimai.nlsearch.converter;

import com.maimai.nlsearch.common.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;

public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus.getValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String s) {
        return OrderStatus.fromString(s);
    }
}
