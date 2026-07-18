package com.chakkihub.controller;

import com.chakkihub.model.Product;
import com.chakkihub.model.Shop;
import com.chakkihub.repository.ProductRepository;
import com.chakkihub.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductRepository productRepo;
    @Autowired private ShopRepository shopRepo;

    // Get all products
    @GetMapping
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    // Get products by shop
    @GetMapping("/shop/{shopId}")
    public List<Product> getByShop(@PathVariable Long shopId) {
        Shop shop = shopRepo.findById(shopId).orElseThrow();
        return productRepo.findAll().stream()
                .filter(p -> p.getShop().equals(shop))
                .toList();
    }

    // Add product to a shop (Admin only)
    @PostMapping("/shop/{shopId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@PathVariable Long shopId, @RequestBody Product product) {
        Shop shop = shopRepo.findById(shopId).orElseThrow();
        product.setShop(shop);
        return productRepo.save(product);
    }
}
