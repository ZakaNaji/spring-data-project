package com.znaji.hibernate.practice.domain.entity;

import com.znaji.hibernate.practice.domain.value.Money;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderTest {

    @Autowired
    private EntityManager em;

    private UserAccount user;
    private Currency USD;

    @BeforeEach
    void setup() {
        USD = Currency.getInstance("USD");

        user = new UserAccount();
        user.setUsername("zaka");
        user.setEmail("zaka@example.com");
        user.setCreatedAt(java.time.Instant.now());
    }

    @Test
    void shouldPersistOrderAndAutoRecalculateTotals() {
        Order order = new Order();
        order.setUserAccount(user);
        order.setStatus(Order.Status.NEW);

        OrderLine line1 = new OrderLine();
        line1.setCapturedSku("SKU-1");
        line1.setCapturedName("Shirt");
        line1.setQuantity(2);
        line1.setUnitPrice(new Money(new BigDecimal("50.00"), USD));

        OrderLine line2 = new OrderLine();
        line2.setCapturedSku("SKU-2");
        line2.setCapturedName("Shoes");
        line2.setQuantity(1);
        line2.setUnitPrice(new Money(new BigDecimal("100.00"), USD));

        order.addOrderLine(line1);
        order.addOrderLine(line2);

        em.persist(user);
        em.persist(order);
        em.flush();

        // subtotal = 200, tax=10, discount=0, grand=210
        assertEquals(new BigDecimal("200.0000"), order.getSubTotal().getAmount());
        assertEquals(new BigDecimal("10.0000"), order.getTaxTotal().getAmount());
        assertEquals(new BigDecimal("210.0000"), order.getGrandTotal().getAmount());
        assertEquals(2, order.getOrderLines().size());
    }

}