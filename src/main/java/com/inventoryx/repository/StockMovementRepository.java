package com.inventoryx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventoryx.entity.StockMovement;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductId(Long productId);
}