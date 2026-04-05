package com.ThreadPractise.demo.Service;

import com.ThreadPractise.demo.DTO.CreateOrderRequest;
import com.ThreadPractise.demo.DTO.OrderResponse;
import com.ThreadPractise.demo.Exceptions.OrderNotFoundException;
import com.ThreadPractise.demo.Model.Order;
import com.ThreadPractise.demo.Model.OrderStatus;
import com.ThreadPractise.demo.Model.Payment;
import com.ThreadPractise.demo.Model.PaymentStatus;
import com.ThreadPractise.demo.Repository.OrderRepository;
import com.ThreadPractise.demo.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        
        validateCreateRequest(createOrderRequest);


        Order order = new Order(
                createOrderRequest.getCustomerEmail(),
                createOrderRequest.getAmount(),
                OrderStatus.CREATED,
                LocalDateTime.now()
        );

        Order saveOrder = orderRepository.save(order);

        Payment payment = new Payment(
                saveOrder.getId(),
                PaymentStatus.PENDING,
                generatePaymentReference(saveOrder.getId()),
                LocalDateTime.now()
        );

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(order, payment);

    }

    private void validateCreateRequest(CreateOrderRequest createOrderRequest) {
        if (createOrderRequest.getAmount() == null || createOrderRequest.getCustomerEmail().isBlank()){
            throw new IllegalArgumentException("Customer email must not be blank");
        }
        if (createOrderRequest.getAmount() == null || createOrderRequest.getAmount().signum() <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private String generatePaymentReference(Long orderId){
        return "PAY-REF"+orderId+"-"+System.currentTimeMillis();
    }

    @Override
    @Cacheable(value = "order-summary", key = "#orderId")
    public OrderResponse getOrderSummary(Long orderId) {
        log.info("Fetching order summary from DB for orderId = " + orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId));

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(()->new OrderNotFoundException(orderId));

        return mapToResponse(order, payment);
    }

    @Override
    @Transactional
    @CachePut(value = "order-summary", key = "#orderId")
    public OrderResponse processOrderAction(Long orderId, String role, String action) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        switch (action.toUpperCase(Locale.ROOT)){
            case "APPROVE_PAYMENT" -> {
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setUpdatedAt(LocalDateTime.now());
                order.setOrderStatus(OrderStatus.CONFIRMED);
            }

            case "CANCEL_ORDER"-> {
                order.setOrderStatus(OrderStatus.CANCELLED);
                payment.setPaymentStatus(PaymentStatus.FAILED);
                payment.setUpdatedAt(LocalDateTime.now());
            }

            case "REFUND_ORDER" ->{
                order.setOrderStatus(OrderStatus.REFUNDED);
                payment.setPaymentStatus(PaymentStatus.REFUNDED);
                payment.setUpdatedAt(LocalDateTime.now());
            }

            default -> throw new IllegalArgumentException("Unsupported Payment Action");

        }

        orderRepository.save(order);
        paymentRepository.save(payment);

        return mapToResponse(order, payment);
    }

    @CacheEvict(value = "order-summary", key = "#orderId")
    public void evictOrderCache(Long orderId){
        System.out.println("Cache evicted for orderId = " + orderId);
    }

    private OrderResponse mapToResponse(Order order, Payment payment){
        return new OrderResponse(
                order.getId(),
                order.getCustomerEmail(),
                order.getAmount(),
                order.getOrderStatus().name(),
                payment.getPaymentStatus().name()
        );
    }


}