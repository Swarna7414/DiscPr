package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.DTO.CreateOrderRequest;
import com.ThreadPractise.demo.DTO.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest createOrderRequest);
    OrderResponse getOrderSummary(Long orderId);
    OrderResponse processOrderAction(Long orderId, String role, String action);
}
