package com.znaji.hibernate.practice.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockItemId {

    private Long variantId;
    private Long warehouseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockItemId that)) return false;
        return Objects.equals(variantId, that.variantId)
                && Objects.equals(warehouseId, that.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variantId, warehouseId);
    }
}
