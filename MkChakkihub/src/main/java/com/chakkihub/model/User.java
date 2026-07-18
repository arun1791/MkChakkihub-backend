package com.chakkihub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role; // ADMIN or CUSTOMER

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;
}
