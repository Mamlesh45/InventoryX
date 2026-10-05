package com.inventoryx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventoryx.entity.Order;
import com.inventoryx.entity.OrderStatus;
import com.inventoryx.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    long countByStatus(OrderStatus status);

    @Query("""
            SELECT COALESCE(SUM(o.totalAmount), 0)
            FROM Order o
            WHERE o.status <> :status
            """)
    Double calculateTotalSales(
            @Param("status") OrderStatus status
    );
}