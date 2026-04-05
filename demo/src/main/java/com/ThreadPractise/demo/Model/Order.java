package com.ThreadPractise.demo.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerEmail;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    public Order() {
    }


    public Order(String customerEmail, BigDecimal amount, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }


}