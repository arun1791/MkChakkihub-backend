package com.chakkihub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;      // EMAIL, SMS, PUSH
    private String message;
    private String status;    // SENT, FAILED
    private LocalDateTime sentAt;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Order order;
}

