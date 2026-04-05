package com.ThreadPractise.demo.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentReference;

    private LocalDateTime updatedAt;


    public Payment(Long orderId, PaymentStatus paymentStatus, String paymentReference, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.paymentReference = paymentReference;
        this.updatedAt = updatedAt;
    }

}