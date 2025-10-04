package com.znaji.hibernate.practice.domain.value;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Embeddable
public class Address {

    private String street;
    private String city;
    private String country;
    private String zipCode;
}
