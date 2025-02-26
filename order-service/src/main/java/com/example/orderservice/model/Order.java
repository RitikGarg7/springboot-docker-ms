package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private String product;
    private Integer quantity;
    private Double price;

    // Store user ID but do NOT use @ManyToOne
    private Long userId;
}

