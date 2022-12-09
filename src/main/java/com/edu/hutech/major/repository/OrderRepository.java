package com.edu.hutech.major.repository;

import com.edu.hutech.major.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
