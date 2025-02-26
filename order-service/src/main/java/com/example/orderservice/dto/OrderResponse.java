package com.example.orderservice.dto;

import com.example.orderservice.model.Order;
import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String product;
    private int quantity;
    private double price;
    private UserResponse user;

    // Constructor
    public OrderResponse(Order order, UserResponse user) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.product = order.getProduct();
        this.quantity = order.getQuantity();
        this.price = order.getPrice();
        this.user = user;
    }
}
