package com.znaji.hibernate.practice.domain.value;

import com.znaji.hibernate.practice.domain.converter.CurrencyConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

@Embeddable
public class Money {

    private final static int SCALE = 4;

    @Column(name = "amount", precision = 19, scale = SCALE, nullable = false)
    private final BigDecimal amount;

    @Convert(converter = CurrencyConverter.class)
    @Column(name = "currency", length = 3, nullable = false)
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and Currency must not be null");
        }
        this.amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    protected Money() {
        this.amount = BigDecimal.ZERO;
        this.currency = Currency.getInstance("USD");
    }

    public Money add(Money other) {
        checkCurrency(other);
        BigDecimal addedAmount = amount.add(other.amount);
        return new Money(addedAmount, currency);
    }

    public Money multiply(int factor) {
        BigDecimal multiplied = amount.multiply(BigDecimal.valueOf(factor));
        return new Money(multiplied, currency);
    }

    private void checkCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new UnsupportedOperationException("currency mismatch");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Money other)) {
            return false;
        }
        return this.amount.equals(other.amount) && this.currency.equals(other.currency);
    }
}
