package com.ThreadPractise.demo.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {
    private String customerEmail;
    private BigDecimal amount;
    private String role;
}