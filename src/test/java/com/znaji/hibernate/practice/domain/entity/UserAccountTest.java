package com.znaji.hibernate.practice.domain.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
class UserAccountTest {

    @Autowired
    private TestEntityManager em;

    @Test
    void testUserAccountHashStability() {
        Set<UserAccount> accounts = new HashSet<>();

        UserAccount user1 = new UserAccount();
        user1.setUsername("user1");
        user1.setEmail("user1@gmail.com");

        //before persisting
        accounts.add(user1);
        System.out.println("Hash before persist: " + user1.hashCode());
        em.persistAndFlush(user1);
        System.out.println("Hash after persist: " + user1.hashCode());
        Assertions.assertTrue(accounts.contains(user1), "Set should contain user1 after persisting");

    }
}
