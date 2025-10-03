package com.znaji.hibernate.practice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
@Entity
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Setter(AccessLevel.NONE)
    @NaturalId(mutable = false)
    @Column(nullable = false, unique = true, updatable = false)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    private void ensureSku() {
        if (this.sku == null || this.sku.isBlank()) {
            this.sku = "SKU-" + java.util.UUID.randomUUID();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sku);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Variant other = (Variant) obj;
        return Objects.equals(sku, other.sku);
    }
}
