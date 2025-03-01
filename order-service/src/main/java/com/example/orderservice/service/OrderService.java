//package com.example.orderservice.service;
//
//import com.example.orderservice.client.UserClient;
//import com.example.orderservice.dto.OrderResponse;
//import com.example.orderservice.dto.UserResponse;
//import com.example.orderservice.model.Order;
//import com.example.orderservice.repository.OrderRepository;
//import io.jsonwebtoken.Jwt;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class OrderService {
//
//    private final OrderRepository orderRepository;
//    private final UserClient userClient;
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//
//
//    public OrderService(OrderRepository orderRepository, UserClient userClient, KafkaTemplate kafkaTemplate) {
//        this.orderRepository = orderRepository;
//        this.userClient = userClient;
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
////    public List<OrderResponse> getAllOrders() {
////        List<Order> orders = orderRepository.findAll();
////        return orders.stream().map(order -> {
////            UserResponse user = userClient.getUserById(order.getUserId());
////            return new OrderResponse(order, user);
////        }).collect(Collectors.toList());
////    }
//
//    public List<OrderResponse> getAllOrders() {
//        if (!isAdmin()) {
//            throw new RuntimeException("Access Denied: Only admins can view all orders.");
//        }
//
//        List<Order> orders = orderRepository.findAll();
//        return orders.stream().map(order -> {
//            UserResponse user = userClient.getUserByUsername(order.getUsername()); // Get user details
//            return new OrderResponse(order, user);
//        }).collect(Collectors.toList());
//    }
//
//    public Order createOrder(Order order) {
//        String username = getAuthenticatedUsername();
//        if (username == null) {
//            throw new RuntimeException("Unauthorized: User not found.");
//        }
//        order.setUsername(username); // Ensure order is linked to the logged-in username
//        Order savedOrder = orderRepository.save(order);
//
//        // Send Audit Log to Kafka
//        String auditMessage = "Order placed: " + savedOrder.getId() + " by User: " + username;
//        kafkaTemplate.send("audit-logs", auditMessage);
//        return savedOrder;
//    }
//
//    // Get all orders for a specific user
//    public List<Order> getOrdersByUsername() {
//        String username = getAuthenticatedUsername();
//        if (username == null) {
//            throw new RuntimeException("Unauthorized: User not found.");
//        }
//        return orderRepository.findByUsername(username);
//    }
//
//    // 🔹 Extract username from SecurityContext
//    private String getAuthenticatedUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            return authentication.getName(); // Username from JWT
//        }
//        return null;
//    }
//
//
//    // 🔹 Check if user is an admin
//    private boolean isAdmin() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            return authentication.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .anyMatch(role -> role.equals("ROLE_ADMIN"));
//        }
//        return false;
//    }
//
//
//}

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

    public OrderService(OrderRepository orderRepository, UserClient userClient, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<OrderResponse> getAllOrders(String roles) {
        if (!isAdmin(roles)) {
            throw new RuntimeException("Access Denied: Only admins can view all orders.");
        }

        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> {
            UserResponse user = userClient.getUserByUsername(order.getUsername());
            return new OrderResponse(order, user);
        }).collect(Collectors.toList());
    }

    public Order createOrder(String username, Order order) {
        if (username == null) {
            throw new RuntimeException("Unauthorized: User not found.");
        }

        order.setUsername(username);
        Order savedOrder = orderRepository.save(order);

        // Send Audit Log to Kafka
        kafkaTemplate.send("audit-logs", "Order placed: " + savedOrder.getId() + " by User: " + username);

        return savedOrder;
    }

    public List<Order> getOrdersByUsername(String username) {
        if (username == null) {
            throw new RuntimeException("Unauthorized: User not found.");
        }
        return orderRepository.findByUsername(username);
    }

    private boolean isAdmin(String roles) {
        return roles.contains("ROLE_ADMIN");
    }
}

