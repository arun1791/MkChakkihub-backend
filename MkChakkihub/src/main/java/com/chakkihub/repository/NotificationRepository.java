package com.chakkihub.repository;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Notification;
import com.chakkihub.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByCustomer(Customer customer);
    List<Notification> findByOrder(Order order);
}
