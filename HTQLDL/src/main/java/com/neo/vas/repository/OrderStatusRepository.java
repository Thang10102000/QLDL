package com.neo.vas.repository;

import com.neo.vas.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 28/05/2021
 */
@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus,Long> {
    OrderStatus findOrderStatusById(Long id);
}
