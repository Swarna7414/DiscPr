package com.ThreadPractise.demo.Repository;

import com.ThreadPractise.demo.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}