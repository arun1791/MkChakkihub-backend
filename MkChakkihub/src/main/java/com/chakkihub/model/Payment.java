package com.chakkihub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;   // UPI, CARD, CASH
    private String status;   // PENDING, SUCCESS, FAILED
    private Double amount;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Customer customer;

    private LocalDateTime paymentTime;
}

