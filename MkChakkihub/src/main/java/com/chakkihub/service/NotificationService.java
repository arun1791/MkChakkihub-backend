package com.chakkihub.service;

import com.chakkihub.model.Customer;
import com.chakkihub.model.Notification;
import com.chakkihub.model.Order;
import com.chakkihub.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired private NotificationRepository notificationRepo;

    // Send a notification (simulate sending for now)
    public Notification sendNotification(Customer customer, Order order, String type, String message) {
        Notification notification = new Notification();
        notification.setCustomer(customer);
        notification.setOrder(order);
        notification.setType(type);
        notification.setMessage(message);
        notification.setStatus("SENT"); // simulate success
        notification.setSentAt(LocalDateTime.now());

        return notificationRepo.save(notification);
    }

    // Get all notifications for a customer
    public List<Notification> getNotificationsByCustomer(Customer customer) {
        return notificationRepo.findByCustomer(customer);
    }

    // Get all notifications for an order
    public List<Notification> getNotificationsByOrder(Order order) {
        return notificationRepo.findByOrder(order);
    }

    // Mark a notification as failed (optional)
    public Notification markAsFailed(Long notificationId, String reason) {
        Notification notification = notificationRepo.findById(notificationId).orElseThrow();
        notification.setStatus("FAILED");
        notification.setMessage(notification.getMessage() + " | Error: " + reason);
        return notificationRepo.save(notification);
    }
}

