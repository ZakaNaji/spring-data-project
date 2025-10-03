package com.znaji.hibernate.practice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Seller {
    @Id
    private Long id;

    @MapsId("id")
    @OneToOne
    @JoinColumn(name = "id")
    private UserAccount userAccount;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "seller", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Shop> shops = new ArrayList<>();

    public List<Shop> getShops() {
        return List.copyOf(shops);
    }
    public void addShop(Shop shop) {
        shops.add(shop);
        shop.setSeller(this);
    }

    public void removeShop(Shop shop) {
        shops.remove(shop);
        shop.setSeller(null);
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

        return id != null && id.equals(((Seller) obj).id);
    }

    @Override
    public int hashCode() {
        return 2001;
    }
}
