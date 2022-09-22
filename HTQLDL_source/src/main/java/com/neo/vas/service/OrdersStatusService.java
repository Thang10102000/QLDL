package com.neo.vas.service;

import com.neo.vas.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersStatusService {
    List<OrderStatus> getAll();
    OrderStatus findById(Long id);
}
