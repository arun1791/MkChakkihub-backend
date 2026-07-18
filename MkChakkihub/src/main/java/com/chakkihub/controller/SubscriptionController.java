package com.chakkihub.controller;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Product;
import com.chakkihub.model.Subscription;
import com.chakkihub.repository.CustomerRepository;
import com.chakkihub.repository.ProductRepository;
import com.chakkihub.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired private SubscriptionRepository subRepo;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private ProductRepository productRepo;

    // Subscribe to a product (Customer only)
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

    // Get all subscriptions for a customer (Customer only)
    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Subscription> getByCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        return subRepo.findAll().stream()
                .filter(s -> s.getCustomer().equals(customer))
                .toList();
    }

    // Cancel a subscription (Customer only)
    @PatchMapping("/{subscriptionId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Subscription cancelSubscription(@PathVariable Long subscriptionId) {
        Subscription sub = subRepo.findById(subscriptionId).orElseThrow();
        sub.setStatus("CANCELLED");
        return subRepo.save(sub);
    }
}
