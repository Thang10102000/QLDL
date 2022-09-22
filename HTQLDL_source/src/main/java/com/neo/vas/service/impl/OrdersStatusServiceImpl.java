package com.neo.vas.service.impl;

import com.neo.vas.domain.OrderStatus;
import com.neo.vas.repository.OrderStatusRepository;
import com.neo.vas.service.OrdersStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrdersStatusServiceImpl implements OrdersStatusService {
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Override
    public List<OrderStatus> getAll() {
        return orderStatusRepository.findAll();
    }

    @Override
    public OrderStatus findById(Long id) {
        return orderStatusRepository.findOrderStatusById(id);
    }
}
