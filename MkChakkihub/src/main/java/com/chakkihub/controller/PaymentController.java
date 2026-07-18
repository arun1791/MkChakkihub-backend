package com.chakkihub.controller;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Order;
import com.chakkihub.model.Payment;
import com.chakkihub.repository.CustomerRepository;
import com.chakkihub.repository.OrderRepository;
import com.chakkihub.service.NotificationService;
import com.chakkihub.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired private PaymentService paymentService;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private NotificationService notificationService;
    @PostMapping("/{customerId}/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Payment pay(@PathVariable Long customerId,
                       @PathVariable Long orderId,
                       @RequestBody Payment payment) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        Order order = orderRepo.findById(orderId).orElseThrow();

        payment.setCustomer(customer);
        payment.setOrder(order);
        payment.setAmount(order.getProduct().getPrice());

        Payment savedPayment = paymentService.processPayment(payment);

        // 🔔 Trigger notification
        notificationService.sendNotification(customer, order, "EMAIL", "Your payment was successful!");

        return savedPayment;
    }

    // View all payments for a customer
    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Payment> getPayments(@PathVariable Long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        return paymentService.getPaymentsByCustomer(customer);
    }
}
