package com.znaji.hibernate.practice.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
