package com.ThreadPractise.demo.Controller;


import com.ThreadPractise.demo.DTO.CreateOrderRequest;
import com.ThreadPractise.demo.DTO.OrderResponse;
import com.ThreadPractise.demo.DTO.UpdatePaymentRequest;
import com.ThreadPractise.demo.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderSummary(@PathVariable Long orderId) {
        return orderService.getOrderSummary(orderId);
    }

    @PostMapping("/{orderId}/actions")
    public OrderResponse processAction(@PathVariable Long orderId,
                                       @RequestBody UpdatePaymentRequest request) {
        return orderService.processOrderAction(orderId, request.getRole(), request.getAction());
    }

}