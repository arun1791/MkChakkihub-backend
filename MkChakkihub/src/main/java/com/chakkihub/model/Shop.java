package com.chakkihub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Shop.java
@Entity
@Table(name = "shops")
@Getter
@Setter
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pincode;
    private Double deliveryRadiusKm;
    private double latitude;
    private double longitude;


    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Product> products;
}
