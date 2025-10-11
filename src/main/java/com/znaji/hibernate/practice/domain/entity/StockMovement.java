package com.znaji.hibernate.practice.domain.entity;

import com.znaji.hibernate.practice.domain.value.StockMovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "variant_id", referencedColumnName = "variant_id"),
            @JoinColumn(name = "warehouse_id", referencedColumnName = "warehouse_id")
    })
    private StockItem stockItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockMovementType movementType;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(length = 100)
    private String reason;

    private String createdBy;

    @CreationTimestamp
    private Instant createdAt;


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof StockMovement that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 2040;
    }
}