package com.ThreadPractise.demo.Exceptions;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long orderId) {
        super("Order not found for id: " + orderId);
    }
}