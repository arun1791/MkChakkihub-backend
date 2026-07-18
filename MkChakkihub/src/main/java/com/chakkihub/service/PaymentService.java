package com.chakkihub.service;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Order;
import com.chakkihub.model.Payment;
import com.chakkihub.repository.OrderRepository;
import com.chakkihub.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    @Autowired private PaymentRepository paymentRepo;
    @Autowired private OrderRepository orderRepo;

    public Payment processPayment(Payment payment) {
        // Save payment
        payment.setStatus("SUCCESS"); // simulate success for now
        payment.setPaymentTime(LocalDateTime.now());
        Payment savedPayment = paymentRepo.save(payment);

        // Update order status
        Order order = payment.getOrder();
        order.setStatus("PAID");
        orderRepo.save(order);

        return savedPayment;
    }

    public List<Payment> getPaymentsByCustomer(Customer customer) {
        return paymentRepo.findByCustomer(customer);
    }
}

