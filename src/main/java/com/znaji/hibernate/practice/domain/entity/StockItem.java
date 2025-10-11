package com.znaji.hibernate.practice.domain.entity;

import com.znaji.hibernate.practice.domain.value.StockMovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class StockItem {

    @EmbeddedId
    private StockItemId id;

    @MapsId("warehouseId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Warehouse warehouse;

    @MapsId("variantId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Variant variant;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal onHand = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal reserved = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal available = BigDecimal.ZERO;

    @OneToMany(mappedBy = "stockItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockMovement> stockMovements = new ArrayList<>();

    // Domain Logic:

    @PrePersist @PreUpdate
    public void checkAvailability() {
        this.available = this.onHand.subtract(this.reserved);
        if (this.available.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Available Stock cannot be less than Zero");
        }
    }

    public void receive(BigDecimal qty, String reason, String actor) {
        validatePositive(qty);
        this.onHand = this.onHand.add(qty);
        addMovement(qty, reason, actor, StockMovementType.RECEIVE);
    }

    public void ship(BigDecimal qty, String reason, String actor) {
        validatePositive(qty);
        ensureEnough(reserved, qty, "reserved");
        this.reserved = this.reserved.subtract(qty);
        this.onHand = this.onHand.subtract(qty);
        addMovement(qty, reason, actor, StockMovementType.SHIP);
    }

    public void reserve(BigDecimal qty, String reason, String actor) {
        validatePositive(qty);
        ensureEnough(available, qty, "Available");
        this.reserved = this.reserved.add(qty);
        addMovement(qty, reason, actor, StockMovementType.RESERVE);
    }

    public void release(BigDecimal qty, String reason, String actor) {
        validatePositive(qty);
        ensureEnough(reserved, qty, "Reserved");
        this.reserved = reserved.subtract(qty);
        addMovement(qty, reason, actor, StockMovementType.RELEASE);
    }

    public void adjust(BigDecimal delta, String reason, String actor) {
        validatePositive(delta);
        this.onHand = this.onHand.add(delta);
        addMovement(delta, reason, actor, StockMovementType.ADJUST);
    }

    private void ensureEnough(BigDecimal toCheck, BigDecimal qty, String label) {
        if (toCheck.compareTo(qty) < 0) {
            throw new IllegalStateException("Not enough " + label + " stock");
        }
    }

    private void validatePositive(BigDecimal qty) {
        if (qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    private void addMovement(BigDecimal qty, String reason, String actor, StockMovementType movementType) {
        StockMovement movement = new StockMovement();
        movement.setStockItem(this);
        movement.setReason(reason);
        movement.setQuantity(qty);
        movement.setCreatedBy(actor);
        movement.setMovementType(movementType);
        this.stockMovements.add(movement);
    }

}
