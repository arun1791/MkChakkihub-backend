package com.chakkihub.controller;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Notification;
import com.chakkihub.model.Order;
import com.chakkihub.repository.CustomerRepository;
import com.chakkihub.repository.OrderRepository;
import com.chakkihub.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private OrderRepository orderRepo;

    // Send notification manually (Admin or Customer)
    @PostMapping("/{customerId}/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public Notification notify(@PathVariable Long customerId,
                               @PathVariable Long orderId,
                               @RequestParam String type,
                               @RequestParam String message) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        Order order = orderRepo.findById(orderId).orElseThrow();

        return notificationService.sendNotification(customer, order, type, message);
    }

    // Get all notifications for a customer
    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Notification> getNotifications(@PathVariable Long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        return notificationService.getNotificationsByCustomer(customer);
    }
}
