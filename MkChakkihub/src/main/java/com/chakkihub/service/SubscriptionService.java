package com.chakkihub.service;

import com.chakkihub.model.Order;
import com.chakkihub.model.Subscription;
import com.chakkihub.model.Customer;
import com.chakkihub.model.Product;
import com.chakkihub.model.Shop;
import com.chakkihub.repository.OrderRepository;
import com.chakkihub.repository.SubscriptionRepository;
import com.chakkihub.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired private SubscriptionRepository subRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private ShopRepository shopRepo;

    // Run every day at 6 AM
    @Scheduled(cron = "0 0 6 * * ?")
    public void autoRenewSubscriptions() {
        List<Subscription> activeSubs = subRepo.findAll()
                .stream()
                .filter(s -> "ACTIVE".equals(s.getStatus()))
                .toList();

        for (Subscription sub : activeSubs) {
            Customer customer = sub.getCustomer();
            Product product = sub.getProduct();

            // For now, pick a default shop (later you can map product → shop)
            Shop shop = shopRepo.findById(1L).orElseThrow();

            Order order = new Order();
            order.setCustomer(customer);
            order.setProduct(product);
            order.setShop(shop);
            order.setStatus("PENDING");
            order.setDeliveryAddress(customer.getAddress());

            orderRepo.save(order);
        }
    }
}
