package com.maimai.nlsearch.converter;

import com.maimai.nlsearch.common.enums.ShippingType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ShippingTypeConverter implements AttributeConverter<ShippingType, String> {
    @Override
    public String convertToDatabaseColumn(ShippingType shippingType) {
        return shippingType.getValue();
    }

    @Override
    public ShippingType convertToEntityAttribute(String s) {
        return ShippingType.fromString(s);
    }
}
