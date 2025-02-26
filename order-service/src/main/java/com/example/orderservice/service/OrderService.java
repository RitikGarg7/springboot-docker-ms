package com.example.orderservice.service;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final KafkaTemplate<String, String> kafkaTemplate;



    public OrderService(OrderRepository orderRepository, UserClient userClient, KafkaTemplate kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> {
            UserResponse user = userClient.getUserById(order.getUserId());
            return new OrderResponse(order, user);
        }).collect(Collectors.toList());
    }

    // Create a new order
    public Order createOrder(Order order) {
//        return orderRepository.save(order);
        UserResponse user = userClient.getUserById(order.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found. Only registered users can place an order.");
        }

        Order savedOrder = orderRepository.save(order);

        // Send Audit Log to Kafka
        String auditMessage = "Order placed: " + savedOrder.getId() + " by User ID: " + order.getUserId();
        kafkaTemplate.send("audit-logs", auditMessage);
        return savedOrder;
    }

    // Get all orders for a specific user
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
