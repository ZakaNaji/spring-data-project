package com.znaji.hibernate.practice.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Currency;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {

    @Override
    public String convertToDatabaseColumn(Currency attribute) {
        return attribute.getCurrencyCode();
    }

    @Override
    public Currency convertToEntityAttribute(String dbData) {
        return Currency.getInstance(dbData);
    }
}
