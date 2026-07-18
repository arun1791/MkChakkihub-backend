package com.chakkihub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Subscription.java
@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String frequency; // DAILY, WEEKLY, MONTHLY
    private String status;    // ACTIVE, CANCELLED

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
