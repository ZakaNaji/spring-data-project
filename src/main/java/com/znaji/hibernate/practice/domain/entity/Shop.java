package com.znaji.hibernate.practice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter()
@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ElementCollection
    @CollectionTable(name = "shop_tags", joinColumns = @JoinColumn(name = "shop_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "shop", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @Embedded
    private Address address;

    public List<Product> getProducts() {
        return List.copyOf(products);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setShop(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setShop(null);
    }

    @Override
    public int hashCode() {
        return 2022;
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
        Shop other = (Shop) obj;
        return id != null && id.equals(other.id);
    }
}
