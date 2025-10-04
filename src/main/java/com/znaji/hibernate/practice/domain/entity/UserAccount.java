package com.znaji.hibernate.practice.domain.entity;

import com.znaji.hibernate.practice.domain.value.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
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
