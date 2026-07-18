package com.chakkihub.controller;

import com.chakkihub.model.Shop;
import com.chakkihub.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shops")
public class ShopController {

    @Autowired private ShopRepository shopRepo;

    // Onboard new shop (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Shop onboard(@RequestBody Shop shop) {
        return shopRepo.save(shop);
    }

    // Update delivery radius (Admin only)
    @PatchMapping("/{shopId}/delivery-radius")
    @PreAuthorize("hasRole('ADMIN')")
    public Shop updateRadius(@PathVariable Long shopId, @RequestParam Double radius) {
        Shop shop = shopRepo.findById(shopId).orElseThrow();
        shop.setDeliveryRadiusKm(radius);
        return shopRepo.save(shop);
    }

    // Get all shops (open to all)
    @GetMapping
    public List<Shop> getAllShops() {
        return shopRepo.findAll();
    }

    // Find shops by pincode (for customers)
    @GetMapping("/pincode/{pincode}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Shop> getShopsByPincode(@PathVariable String pincode) {
        return shopRepo.findAll().stream()
                .filter(s -> s.getPincode().equals(pincode))
                .toList();
    }
}
