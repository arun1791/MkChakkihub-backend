package com.chakkihub.controller;

import com.chakkihub.model.*;
import com.chakkihub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private SubscriptionRepository subRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private ProductRepository productRepo;

    // Register new customer (linked to User entity)
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public Customer register(@RequestBody Customer customer) {
        return customerRepo.save(customer);
    }

    // View customer profile
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Customer getCustomer(@PathVariable Long id) {
        return customerRepo.findById(id).orElseThrow();
    }

    // Subscribe to product (link customer + product)
    @PostMapping("/{customerId}/subscribe/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Subscription subscribe(@PathVariable Long customerId,
                                  @PathVariable Long productId,
                                  @RequestBody Subscription sub) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        sub.setCustomer(customer);
        sub.setProduct(product);
        sub.setStatus("ACTIVE");
        return subRepo.save(sub);
    }

    // Place order (link customer + shop + product)
    @PostMapping("/{customerId}/order/{shopId}/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order placeOrder(@PathVariable Long customerId,
                            @PathVariable Long shopId,
                            @PathVariable Long productId,
                            @RequestBody Order order) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        Shop shop = shopRepo.findById(shopId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        order.setCustomer(customer);
        order.setShop(shop);
        order.setProduct(product);
        order.setStatus("PENDING");
        return orderRepo.save(order);
    }

    // Track orders for a customer
    @GetMapping("/{customerId}/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Order> getOrders(@PathVariable Long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        return orderRepo.findAll().stream()
                .filter(o -> o.getCustomer().equals(customer))
                .toList();
    }
}
