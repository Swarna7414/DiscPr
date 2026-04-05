package com.ThreadPractise.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String customerEmail;
    private BigDecimal amount;
    private String orderStatus;
    private String paymentStatus;
}