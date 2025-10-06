package com.znaji.hibernate.practice.domain.entity;

import com.znaji.hibernate.practice.domain.value.Money;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

@Entity
@Table(name = "orders")
@SQLDelete(sql = "update orders set deleted = true where id= ?")
@SQLRestriction("deleted = false")
@Audited
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Order {
    enum Status { NEW, PAID, SHIPPED, CANCELLED, REFUNDED }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean deleted = false;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "sub_total_amount", precision = 19, scale = 4, nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "sub_total_currency", length = 3, nullable = false))
    })
    private Money subTotal;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "discount_total_amount", precision = 19, scale = 4, nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "discount_total_currency", length = 3, nullable = false))
    })
    private Money discountTotal;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "tax_total_amount", precision = 19, scale = 4, nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "tax_total_currency", length = 3, nullable = false))
    })
    private Money taxTotal;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "grand_total_amount", precision = 19, scale = 4, nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "grand_total_currency", length = 3, nullable = false))
    })
    private Money grandTotal;


    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    public List<OrderLine> getOrderLines() {
        return List.copyOf(orderLines);
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
        orderLine.setOrder(this);
    }

    public void removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
        orderLine.setOrder(null);
    }

    @PrePersist
    @PreUpdate
    public void recalculateTotal() {
        if (orderLines.isEmpty()) {
            throw new IllegalStateException("Cannot calculate totals: order has no order lines.");
        }

        Currency baseCurrency = orderLines.getFirst().getUnitPrice().getCurrency();

        //subTotal:
        Money subTotal = new Money(BigDecimal.ZERO, baseCurrency);
        for (var line : orderLines) {
            if (!line.getUnitPrice().getCurrency().equals(baseCurrency)) {
                throw new IllegalStateException("Mixed currencies detected within order lines.");
            }
            subTotal = subTotal.add(line.getLineTotal());
        }
        this.subTotal = subTotal;

        //discount
        //TODO-DISCOUNT: to impl the real discount logic, after adding the discount entity
        this.discountTotal = new Money(BigDecimal.ZERO, baseCurrency);

        //tax:
        //TODO-TAX: impl real logic latter:
        BigDecimal taxRate = new BigDecimal("0.05");
        this.taxTotal = new Money(subTotal.getAmount().multiply(taxRate), baseCurrency);

        //grandTotal:
        this.grandTotal = subTotal.add(taxTotal).sub(discountTotal);

        //guaranty prices harmony:
        guarantyPricesHarmony();
    }

    public void guarantyPricesHarmony() {
        if (subTotal == null || taxTotal == null || discountTotal == null || grandTotal == null) {
            throw new IllegalStateException("Order totals must all be initialized before saving");
        }

        if (!allSameCurrency(subTotal, discountTotal, taxTotal, grandTotal)) {
            throw new IllegalStateException("Inconsistent currencies across totals");
        }

        Money expected = subTotal.add(taxTotal).sub(discountTotal);

        if (!expected.equals(grandTotal)) {
            throw new IllegalStateException(
                    String.format("Order total mismatch: expected %s but found %s", expected, grandTotal)
            );
        }
    }

    private boolean allSameCurrency(Money... monies) {
        Currency first = monies[0].getCurrency();
        return Arrays.stream(monies).allMatch(m -> m.getCurrency().equals(first));
    }

    @Override
    public String toString() {
        return String.format(
                "Order{id=%d, status=%s, subTotal=%s, discountTotal=%s, taxTotal=%s, grandTotal=%s}",
                id, status, subTotal, discountTotal, taxTotal, grandTotal
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        Order other = (Order) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 2005;
    }
}
