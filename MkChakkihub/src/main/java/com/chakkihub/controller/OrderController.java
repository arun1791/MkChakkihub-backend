package com.chakkihub.controller;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Order;
import com.chakkihub.model.Shop;
import com.chakkihub.model.Product;
import com.chakkihub.repository.CustomerRepository;
import com.chakkihub.repository.OrderRepository;
import com.chakkihub.repository.ShopRepository;
import com.chakkihub.repository.ProductRepository;
import com.chakkihub.service.NotificationService;
import com.chakkihub.utils.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderRepository orderRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private NotificationService notificationService;
    @PostMapping("/quick/{customerId}/{shopId}/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order placeQuickOrder(@PathVariable Long customerId,
                                 @PathVariable Long shopId,
                                 @PathVariable Long productId,
                                 @RequestBody Order order) {

        Customer customer = customerRepo.findById(customerId).orElseThrow();
        Shop shop = shopRepo.findById(shopId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        double distance = DistanceUtil.calculateDistance(
                customer.getLatitude(), customer.getLongitude(),
                shop.getLatitude(), shop.getLongitude()
        );

        if (distance > shop.getDeliveryRadiusKm()) {
            throw new RuntimeException("Delivery not available in your area.");
        }

        order.setCustomer(customer);
        order.setShop(shop);
        order.setProduct(product);
        order.setStatus("PENDING");

        Order savedOrder = orderRepo.save(order);

        // 🔔 Trigger notification
        notificationService.sendNotification(customer, savedOrder, "EMAIL", "Your order has been placed successfully!");

        return savedOrder;
    }


    // Track order by ID
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order trackOrder(@PathVariable Long orderId) {
        return orderRepo.findById(orderId).orElseThrow();
    }

    @PatchMapping("/{orderId}/deliver")
    @PreAuthorize("hasRole('ADMIN')") // or DELIVERY role if you add one
    public Order markAsDelivered(@PathVariable Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus("DELIVERED");
        Order savedOrder = orderRepo.save(order);

        // 🔔 Trigger notification
        notificationService.sendNotification(
                order.getCustomer(),
                savedOrder,
                "EMAIL",
                "Your order #" + order.getId() + " has been delivered!"
        );

        return savedOrder;
    }


}
