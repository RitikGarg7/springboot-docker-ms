//package com.example.orderservice.controller;
//
//import com.example.orderservice.dto.OrderResponse;
//import com.example.orderservice.model.Order;
//import com.example.orderservice.service.OrderService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/orders")
//public class OrderController {
//
//    private final OrderService orderService;
//
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//    @GetMapping
//    public List<OrderResponse> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//
//    @PostMapping
//    public Order createOrder(@RequestBody Order order) {
//        return orderService.createOrder(order);
//    }
//
//    @GetMapping("/user/{userId}")
//    public List<Order> getOrdersByUsername() {
//        return orderService.getOrdersByUsername();
//    }
//}



package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders(@RequestHeader("X-User-Roles") String roles) {
        return orderService.getAllOrders(roles);
    }

    @PostMapping
    public Order createOrder(@RequestHeader("X-Authenticated-User") String username, @RequestBody Order order) {
        return orderService.createOrder(username, order);
    }

    @GetMapping("/user")
    public List<Order> getOrdersByUsername(@RequestHeader("X-Authenticated-User") String username) {
        return orderService.getOrdersByUsername(username);
    }
}
