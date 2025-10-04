package com.znaji.hibernate.practice.domain.entity;

import com.znaji.hibernate.practice.domain.value.Address;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String email;

    private Instant createdAt;

    @OneToOne(mappedBy = "userAccount")
    private Seller seller;

    @Embedded
    private Address address;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUserAccount(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUserAccount(null);
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
        UserAccount other = (UserAccount) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 2002;
    }
}
