package com.chakkihub.repository;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Order;
import com.chakkihub.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCustomer(Customer customer);
    List<Payment> findByOrder(Order order);
}

