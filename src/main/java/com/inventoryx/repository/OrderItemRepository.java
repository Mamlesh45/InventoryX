package com.inventoryx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventoryx.entity.Order;
import com.inventoryx.entity.OrderItem;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

}