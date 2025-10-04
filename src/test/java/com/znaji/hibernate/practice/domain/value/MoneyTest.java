package com.znaji.hibernate.practice.domain.value;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void testMoneyValueObject() {
        Money usd10 = new Money(new BigDecimal("10.00"), Currency.getInstance("USD"));
        Money usd20 = new Money(new BigDecimal("20.00"), Currency.getInstance("USD"));
        Money eur20 = new Money(new BigDecimal("20.00"), Currency.getInstance("EUR"));

        assertEquals(usd10.add(usd20), new Money(new BigDecimal("30.0000"), Currency.getInstance("USD")));
        assertThrows(UnsupportedOperationException.class, () -> usd10.add(eur20));

    }

}