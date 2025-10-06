package com.znaji.hibernate.practice.domain.entity;

import com.znaji.hibernate.practice.domain.value.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "order_line")
@SQLRestriction("deleted = false")
@SQLDelete(sql = "update order_line set deleted = false where id = ?")
@Audited
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int quantity;

    @Embedded
    private Money unitPrice;

    private String capturedSku;

    private String capturedName;

    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public Money getLineTotal() {
        return unitPrice.multiply(quantity);
    }

    @Override
    public int hashCode() {
        return 2006;
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
        OrderLine other = (OrderLine) o;
        return this.id != null && this.id.equals(other.id);
    }
}
